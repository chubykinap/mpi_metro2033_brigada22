package b22.metro2033.Controller.Delivery;

import b22.metro2033.Entity.Army.*;
import b22.metro2033.Entity.Delivery.Courier;
import b22.metro2033.Entity.Delivery.DeliveryOrder;
import b22.metro2033.Entity.Delivery.DeliveryState;
import b22.metro2033.Entity.Delivery.OrderItem;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.OrderUtility;
import b22.metro2033.Entity.Utility.SoldierUtility;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.Delivery.OrderRepository;
import b22.metro2033.Repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/courier")
public class CourierController {
    private final UserRepository userRepository;
    private final CourierRepository courierRepository;

    @Autowired
    public CourierController(UserRepository userRepository,
                             CourierRepository courierRepository) {
        this.userRepository = userRepository;
        this.courierRepository = courierRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('delivery:write')")
    public String index(Model model, Authentication authentication){
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if(user == null){
            return "redirect:/auth/login";
        }
        List<Courier> couriers = courierRepository.findAll();
        model.addAttribute("couriers", couriers);

        return "courier/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('delivery:write')")
    public String createForm(Model model, Authentication authentication){
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if(user == null)
            return "redirect:/auth/login";

        model.addAttribute("courier", new Courier());
        model.addAttribute("action", "Create");
        List<User> users = userRepository.findFreeCourier();
        if (users.size() == 0)
            return "redirect:/courier";
        model.addAttribute("users", users);

        return "courier/form";
    }

    @PreAuthorize("hasAuthority('delivery:write')")
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String create(@RequestBody @Valid String response) throws Exception {
        try {
            JSONObject json = new JSONObject(response);

            long user_id = Long.parseLong(json.getString("user_id"));
            Courier courier = new Courier();
            User user = userRepository.findById(user_id).orElse(null);
            if (user != null) {
                courier.setUser(user);
                courierRepository.save(courier);
            }
            return "redirect:/";
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "HTTP request is wrong (CODE 400)\n");
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('delivery:write')")
    public String remove(@PathVariable Long id) {

        Courier courier = courierRepository.findById(id).orElse(null);

        if(courier != null)
            courierRepository.deleteById(id);

        return "redirect:/courier";
    }

    private List<Role> getRolesForSelect(Authentication authentication){
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);

        List<Role> roles = new ArrayList<>();

        if(user == null){
            return roles;
        }

        switch (user.getRole()){
            case HEAD_COURIER:
                roles = Stream.of(Role.COURIER).collect(Collectors.toList());
                break;
            case ADMIN:
                roles = Stream.of(Role.HEAD_COURIER, Role.COURIER).collect(Collectors.toList());
                break;
        }

        return roles;
    }
}

