package b22.metro2033.Controller.Delivery;

import b22.metro2033.Entity.Alerts.AlertMessages;
import b22.metro2033.Entity.Alerts.TypeOfMessage;
import b22.metro2033.Entity.Delivery.*;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.OrderItemUtility;
import b22.metro2033.Entity.Utility.OrderUtility;
import b22.metro2033.Repository.Alerts.AlertsRepository;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.Delivery.OrderRepository;
import b22.metro2033.Repository.UserRepository;
import org.json.JSONArray;
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
    public String index(Model model, Authentication authentication) {
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }
        List<DeliveryOrder> orders;
        if (user.getRole() == Role.COURIER) {
            orders = orderRepository.findAllByCourierId(user.getCourier().getId());

        } else
            orders = orderRepository.findAll();
        model.addAttribute("orders", OrderUtility.toOrderUtility(orders));
        return "delivery/index";
    }

    @GetMapping("create/{type}")
    @PreAuthorize("hasAuthority('delivery:write')")
    public String create(Model model, Authentication authentication, @PathVariable String type) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        model.addAttribute("order", new DeliveryOrder());

        List<Courier> couriers = courierRepository.findAllByWorkingFalse();
        model.addAttribute("couriers", couriers);

        model.addAttribute("stations", stations);
        if (type.equals("send"))
            return "delivery/form_send";
        else
            return "delivery/form_receive";
    }

    @PreAuthorize("hasAuthority('delivery:write')")
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String create(@RequestBody @Valid String response) throws Exception {
        JSONObject json = new JSONObject(response);

        String station = json.getString("station");
        boolean direction = json.getBoolean("direction");
        Date date = parseStringToDate(json.getString("date"));
        Courier courier = courierRepository.findById(json.getInt("courier_id")).orElse(null);
        if (courier == null)
            return "redirect:/delivery";
        DeliveryState state = DeliveryState.RECEIVED;
        List<OrderItemUtility> items = jsonToList(json.getJSONArray("items"));

        DeliveryOrder order = new DeliveryOrder();
        order.setState(state);
        order.setCourier(courier);
        order.setPointOfDeparture(direction);
        order.setStation(station);
        order.setDate(date);
        orderRepository.save(order);

        courier.setOrder(order);
        courier.setWorking(true);

        for (OrderItemUtility item : items) {
            Item item_stored = itemRepository.findByName(item.getItem());
            if (direction &&
                    item_stored.getQuantity() < item.getQuantity()) {
                orderRepository.delete(order);
                throw new Exception("Недостаточно ресурсов");
            } else if (direction) {
                item_stored.setQuantity(item_stored.getQuantity() - item.getQuantity());
                itemRepository.save(item_stored);
            }
            OrderItem d_o = new OrderItem();
            d_o.setOrder(order);
            d_o.setQuantity(item.getQuantity());
            d_o.setItem(item_stored);
            orderItemRepository.save(d_o);
        }

        String message = "Вам назначен новый заказ";
        sendAlertMessage(courier.getUser(), message, TypeOfMessage.NOTIFICATION);
        orderRepository.save(order);
        courierRepository.save(courier);

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

        if (!order.isPointOfDeparture() && state == DeliveryState.COMPLETED) {
            List<OrderItem> orderItems = orderItemRepository.findAllByIdOrderId(order.getId());
            for (OrderItem oi : orderItems) {
                Item item_stored = itemRepository.findById(oi.getItem().getId());
                item_stored.setQuantity(item_stored.getQuantity() + oi.getQuantity());
                itemRepository.save(item_stored);
            }
            state = DeliveryState.CLOSED;
        }

        order.setState(state);
        orderRepository.save(order);
        return "redirect:/delivery";
    }

    @RequestMapping(value = "/delete/{id}")
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
            courier.setOrder(null);
            courier.setWorking(false);
            orderRepository.delete(order);
        }

        return "redirect:/delivery";
    }

    private Date parseStringToDate(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH);
        return format.parse(date);
    }

    private List<OrderItemUtility> jsonToList(JSONArray array) throws JSONException {
        List<OrderItemUtility> items = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONArray tmp = (JSONArray) array.get(i);
            OrderItemUtility order = new OrderItemUtility(
                    tmp.get(0).toString(),
                    Integer.parseInt(tmp.get(1).toString()));
            items.add(order);
        }

        return items;
    }

    public void sendAlertMessage(User user, String message, TypeOfMessage type) {
        AlertMessages alertMessages = new AlertMessages();
        alertMessages.setUser(user);
        alertMessages.setAlert_message(message);
        alertMessages.setType_of_message(type);

        alertsRepository.save(alertMessages);
    }
}
