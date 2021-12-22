package b22.metro2033.Controller.Delivery;

import b22.metro2033.Entity.Delivery.*;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.OrderItemUtility;
import b22.metro2033.Entity.Utility.OrderUtility;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.Delivery.OrderRepository;
import b22.metro2033.Repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/delivery")
public class OrderController {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final static String[] stations = new String[]{
            "Горьковская", "Спасская", "Адмиралтейская", "Маяковская"
    };

    public OrderController(UserRepository userRepository,
                           OrderRepository orderRepository,
                           CourierRepository courierRepository,
                           OrderItemRepository repository,
                           ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
        this.orderItemRepository = repository;
        this.itemRepository = itemRepository;
    }


    @GetMapping
    @PreAuthorize("hasAuthority('delivery:read')")
    public String index(Model model, Authentication authentication) {
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }
        Courier courier = courierRepository.findById(user.getId()).orElse(null);
        List<DeliveryOrder> orders;
        if (user.getRole() == Role.COURIER || user.getRole() == Role.HEAD_COURIER)
            orders = orderRepository.findAllByCourierId(courier.getId());
        else
            orders = orderRepository.findAll();
        model.addAttribute("orders", OrderUtility.toOrderUtility(orders));
        return "delivery/index";
    }

    @GetMapping("create")
    @PreAuthorize("hasAuthority('delivery:write')")
    public String create(Model model, Authentication authentication) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        model.addAttribute("order", new DeliveryOrder());

        List<Courier> couriers = courierRepository.findAllByWorkingFalse();
        model.addAttribute("couriers", couriers);

        model.addAttribute("stations", stations);

        return "delivery/form";
    }

    @PreAuthorize("hasAuthority('delivery:write')")
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String create(@RequestBody @Valid String response) throws Exception {
        JSONObject json = new JSONObject(response);

        String station = json.getString("station");
        boolean direction = json.getBoolean("direction");
        Date date = parseStringToDate(json.getString("date"));
        DeliveryState state = DeliveryState.RECEIVED;
        ObjectMapper mapper = new ObjectMapper();
        String test = json.getString("items");
        List<OrderItemUtility> items = mapper.readValue(json.getString("items"),
                new TypeReference<List<OrderItemUtility>>() {
                });

        DeliveryOrder order = new DeliveryOrder();
        order.setState(state);
        order.setPointOfDeparture(direction);
        order.setStation(station);
        order.setDate(date);
        orderRepository.save(order);

        for (OrderItemUtility item : items) {
            Item item_stored = itemRepository.findByName(item.getItem());
            if (direction &&
                    item_stored.getQuantity() < item.getQuantity())
                throw new Exception("Недостаточно ресурсов");
            OrderItem d_o = new OrderItem();
            d_o.setOrder(order);
            d_o.setQuantity(item.getQuantity());
            d_o.setItem(item_stored);
            orderItemRepository.save(d_o);
        }

        return "redirect:/delivery";
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

        Courier courier = courierRepository.findByOrderId(order.getId());

        List<OrderItem> list = orderItemRepository.findAllByIdOrderId(order.getId());
        model.addAttribute("courier", courier);
        model.addAttribute("order", new OrderUtility(order));
        model.addAttribute("states", DeliveryState.getHigher(order.getState()));
        model.addAttribute("items", list);
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

        order.setState(state);
        orderRepository.save(order);
        return "redirect:/delivery";
    }

    @RequestMapping(value = "/completeOrder", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('delivery:read')")
    public String completeOrder(@PathVariable Long id) {
        DeliveryOrder order = orderRepository.findById(id).orElse(null);

        if (order != null) {
            order.setState(DeliveryState.COMPLETED);
            List<OrderItem> list = orderItemRepository.findAllByIdOrderId(id);
            if (!order.isPointOfDeparture())
                for (OrderItem item : list) {
                    Item item_stored = itemRepository.findByName(item.getItem().getName());
                    item_stored.setQuantity(item.getQuantity() + item.getQuantity());
                    itemRepository.save(item_stored);
                }
            orderRepository.save(order);
        }

        return "redirect:/delivery";
    }

    @RequestMapping(value = "/deleteOrder", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('delivery:read')")
    public String deleteOrder(@PathVariable Long id) {
        DeliveryOrder order = orderRepository.findById(id).orElse(null);

        if (order != null) {
            orderRepository.delete(order);
        }

        return "redirect:/delivery";
    }

    private Date parseStringToDate(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH);
        return format.parse(date);
    }
}
