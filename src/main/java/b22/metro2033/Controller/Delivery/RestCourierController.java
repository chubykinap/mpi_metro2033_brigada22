package b22.metro2033.Controller.Delivery;

import b22.metro2033.Entity.Delivery.Courier;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.UserRepository;
import b22.metro2033.domain.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/courier", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestCourierController {
    private final UserRepository userRepository;
    private final CourierRepository courierRepository;

    public RestCourierController(UserRepository userRepository, CourierRepository courierRepository) {
        this.userRepository = userRepository;
        this.courierRepository = courierRepository;
    }

    @PreAuthorize("hasAuthority('delivery:write')")
    @RequestMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response create(@RequestBody String response) throws Exception {
        JSONObject json = new JSONObject(response);

        long user_id = Long.parseLong(json.getString("user_id"));
        Courier courier = new Courier();
        User user = userRepository.findById(user_id).orElse(null);

        if (user == null){
            return new Response("Error", "Пользователь не найден!");
        }

        if (user.getCourier() != null || user.getSoldier() != null){
            return new Response("Error", "Пользователь уже назначен!");
        }

        courier.setUser(user);
        courierRepository.save(courier);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("login", user.getLogin());
        hashMap.put("name", user.getName());
        hashMap.put("surname", user.getSurname());

        return new Response("Done", hashMap);
    }

    @PreAuthorize("hasAuthority('delivery:write')")
    @RequestMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response remove(@RequestBody String response) throws JSONException {

        JSONObject json = new JSONObject(response);

        long courier_id = Long.parseLong(json.getString("courier_id"));

        String login;
        String name;
        String surname;

        Courier courier = courierRepository.findById(courier_id).orElse(null);

        if(courier == null){
            return new Response("Error", "Курьер не найден!");
        }

        if(courier.getOrder() != null){
            return new Response("Error", "На курьера назначен заказ!");
        }

        login = courier.getUser().getLogin();
        name = courier.getUser().getName();
        surname = courier.getUser().getSurname();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("login", login);
        hashMap.put("name", name);
        hashMap.put("surname", surname);

        courierRepository.deleteById(courier_id);

        return new Response("Done", hashMap);
    }

}
