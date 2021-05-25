package b22.metro2033.Controller.Delivery;

import b22.metro2033.Entity.Delivery.Courier;
import b22.metro2033.Entity.Delivery.DeliveryOrder;
import b22.metro2033.Entity.Delivery.DeliveryState;
import b22.metro2033.Entity.Delivery.OrderItem;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.OrderUtility;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.Delivery.OrderRepository;
import b22.metro2033.Repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final OrderItemRepository repository;

    @Autowired
    public DeliveryController(UserRepository userRepository,
                              OrderRepository orderRepository,
                              CourierRepository courierRepository, OrderItemRepository repository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
        this.repository = repository;
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
        List<OrderItem> list = repository.findAllByIdOrderId(order.getId());
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
