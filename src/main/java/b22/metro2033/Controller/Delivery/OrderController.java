package b22.metro2033.Controller.Delivery;

import b22.metro2033.Entity.Delivery.*;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.OrderUtility;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.Delivery.OrderRepository;
import b22.metro2033.Repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RequestMapping("/delivery")
public class OrderController {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final OrderItemRepository repository;
    private final ItemRepository itemRepository;

    public OrderController(UserRepository userRepository,
                           OrderRepository orderRepository,
                           CourierRepository courierRepository,
                           OrderItemRepository repository,
                           ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
        this.repository = repository;
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
            orders = orderRepository.findAllByCouriersId(courier.getId());
        else
            orders = orderRepository.findAll();
        model.addAttribute("orders", OrderUtility.toOrderUtility(orders));
        return "delivery/index";
    }

    @GetMapping("create")
    @PreAuthorize("hasAuthority('delivery:write')")
    public String create(Model model, Authentication authentication){
        model.addAttribute("order", new DeliveryOrder());
        model.addAttribute("action", "Create");
        List<User> users = userRepository.findAllByRoleIn(Collections.singletonList(Role.COURIER));
        if (users.size() == 0){
            return "order/form";
        }

        model.addAttribute("users", users);
        model.addAttribute("stations", Stations.values());
        model.addAttribute("items", itemRepository.findAll());

        return "order/form";
    }

    @PreAuthorize("hasAuthority('delivery:write')")
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String create(@RequestBody @Valid String response) throws Exception { //ParesException type?
        JSONObject json = new JSONObject(response);

        long user_id = Long.parseLong(json.getString("user_id"));

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

        List<Courier> couriers = courierRepository.findAllByOrdersId(order.getId());

        List<OrderItem> list = repository.findAllByIdOrderId(order.getId());
        model.addAttribute("couriers", couriers);
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
}
