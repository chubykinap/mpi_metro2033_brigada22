package b22.metro2033.Controller.Delivery;

import b22.metro2033.Entity.Delivery.*;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.OrderUtility;
import b22.metro2033.Repository.Alerts.AlertsRepository;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.Delivery.OrderRepository;
import b22.metro2033.Repository.UserRepository;
import b22.metro2033.Service.PaginatedService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/delivery")
public class OrderController {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final AlertsRepository alertsRepository;
    private final static String[] stations = new String[]{
            "Горьковская", "Спасская", "Адмиралтейская", "Маяковская"
    };

    public OrderController(UserRepository userRepository,
                           OrderRepository orderRepository,
                           CourierRepository courierRepository,
                           OrderItemRepository repository,
                           ItemRepository itemRepository,
                           AlertsRepository alertsRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
        this.orderItemRepository = repository;
        this.itemRepository = itemRepository;
        this.alertsRepository = alertsRepository;
    }


    @GetMapping
    @PreAuthorize("hasAuthority('delivery:read')")
    public String index(Model model, Authentication authentication,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size,
                        @RequestParam("start_page") Optional<Integer> start_page,
                        @RequestParam("number_of_pages") Optional<Integer> number_of_pages) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        int startPage = start_page.orElse(1);
        int numberOfPages = number_of_pages.orElse(10);

        if (startPage < 0) startPage = 1;
        if (currentPage < 0) currentPage = 1;

        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }
        List<DeliveryOrder> orders;
        if (user.getRole() == Role.COURIER) {
            orders = orderRepository.findAllByCourierId(user.getCourier().getId());
        } else
            orders = orderRepository.findAll();

        //Page<DeliveryOrder> ordersPage = PaginatedService.findPaginated(PageRequest.of(currentPage - 1, pageSize), orders);

        List<OrderUtility> ordersUtility = OrderUtility.toOrderUtility(orders);

        Page<OrderUtility> ordersPage = PaginatedService.findPaginated(PageRequest.of(currentPage - 1, pageSize), ordersUtility);

        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("start_page", startPage);
        model.addAttribute("number_of_pages", numberOfPages);
        model.addAttribute("current_page", currentPage);

        int totalPages = ordersPage.getTotalPages();

        if (totalPages > 0) {

            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = startPage; i < startPage + numberOfPages; i++) {
                if (i > totalPages) break;
                pageNumbers.add(i);
            }
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "delivery/index";
    }

    @GetMapping("create/{type}")
    @PreAuthorize("hasAuthority('delivery:write')")
    public String create(Model model, Authentication authentication, @PathVariable String type) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        model.addAttribute("order", new DeliveryOrder());

        List<Courier> couriers = courierRepository.findAllByOrder(null);
        if (couriers.size() == 0)
            model.addAttribute("couriers", "NoData");
        else
            model.addAttribute("couriers", couriers);

        model.addAttribute("stations", stations);
        if (type.equals("send"))
            return "delivery/form_send";
        else
            return "delivery/form_receive";
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("hasAuthority('delivery:read')")
    public String viewOrder(Model model, Authentication authentication, @PathVariable("id") long id) {
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }

        DeliveryOrder order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return "/delivery";
        }
        model.addAttribute("courier", order.getCourier());
        model.addAttribute("order", new OrderUtility(order));
        model.addAttribute("states", DeliveryState.getHigher(order.getState()));
        model.addAttribute("items", order.getOrderItems());
        return "/delivery/view";
    }

    @RequestMapping(value = "/changeState", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('delivery:read')")
    public String changeState(@RequestBody String request) throws JSONException {
        JSONObject json = new JSONObject(request);
        DeliveryState state = DeliveryState.valueOf(json.getString("state"));
        long id = Long.parseLong(json.getString("id"));

        DeliveryOrder order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return "redirect:/delivery";
        }

        if (!order.isPointOfDeparture() && state == DeliveryState.COMPLETED) {
            List<OrderItem> orderItems = orderItemRepository.findAllByIdOrderId(order.getId());
            for (OrderItem oi : orderItems) {
                Item item_stored = itemRepository.findById(oi.getItem().getId());
                item_stored.setQuantity(item_stored.getQuantity() + oi.getQuantity());
                itemRepository.save(item_stored);
            }
            state = DeliveryState.CLOSED;
            Courier courier = order.getCourier();
            courier.setOrder(null);
        }

        order.setState(state);
        orderRepository.save(order);
        return "redirect:/delivery";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('delivery:write')")
    public String deleteOrder(@PathVariable Long id) {
        DeliveryOrder order = orderRepository.findById(id).orElse(null);

        if (order == null) {
            return "redirect:/delivery";
        } else {
            if (order.isPointOfDeparture() && order.getState() != DeliveryState.COMPLETED) {
                List<OrderItem> orderItems = orderItemRepository.findAllByIdOrderId(order.getId());
                for (OrderItem oi : orderItems) {
                    Item item_stored = itemRepository.findById(oi.getItem().getId());
                    item_stored.setQuantity(item_stored.getQuantity() + oi.getQuantity());
                    itemRepository.save(item_stored);
                }
            }
            Courier courier = courierRepository.findByOrderId(order.getId());
            if (courier != null)
                courier.setOrder(null);
            orderRepository.delete(order);
        }

        return "redirect:/delivery";
    }
}
