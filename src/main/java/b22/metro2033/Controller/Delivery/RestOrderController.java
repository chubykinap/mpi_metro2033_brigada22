package b22.metro2033.Controller.Delivery;

import b22.metro2033.Entity.Alerts.AlertMessages;
import b22.metro2033.Entity.Alerts.TypeOfMessage;
import b22.metro2033.Entity.Delivery.*;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.OrderItemUtility;
import b22.metro2033.Repository.Alerts.AlertsRepository;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.Delivery.OrderRepository;
import b22.metro2033.Repository.UserRepository;
import b22.metro2033.domain.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "/delivery", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestOrderController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final AlertsRepository alertsRepository;
    private final static String[] stations = new String[]{
            "Горьковская", "Спасская", "Адмиралтейская", "Маяковская"
    };

    public RestOrderController(UserRepository userRepository,
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

    @PreAuthorize("hasAuthority('delivery:write')")
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response create(@RequestBody @Valid String response) throws Exception {
        JSONObject json = new JSONObject(response);

        String station = json.getString("station");
        boolean direction = json.getBoolean("direction");
        Date date = parseStringToDate(json.getString("date"));
        Courier courier = courierRepository.findById(json.getInt("courier_id")).orElse(null);
        if (courier == null)
            return new Response("Error", "Курьер не найден");
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

        for (OrderItemUtility item : items) {
            //Вставить проверку
            Item item_stored = itemRepository.findByName(item.getItem()).orElse(null);
            if (direction &&
                    item_stored.getQuantity() < item.getQuantity()) {
                orderRepository.delete(order);
                return new Response("Error","Недостаточно ресурсов");
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

        return new Response("Done", "");
    }

    public void sendAlertMessage(User user, String message, TypeOfMessage type) {
        AlertMessages alertMessages = new AlertMessages();
        alertMessages.setUser(user);
        alertMessages.setAlert_message(message);
        alertMessages.setType_of_message(type);

        alertsRepository.save(alertMessages);
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
}
