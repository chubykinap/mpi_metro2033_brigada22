package b22.metro2033.Controller;

import b22.metro2033.Entity.Alerts.AlertMessages;
import b22.metro2033.Entity.Army.MovementSensor;
import b22.metro2033.Entity.Army.SensorStatus;
import b22.metro2033.Entity.Army.Soldier;
import b22.metro2033.Entity.Delivery.Courier;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.SoldierUtility;
import b22.metro2033.Repository.Army.MovementSensorRepository;
import b22.metro2033.Repository.Army.PostRepository;
import b22.metro2033.Repository.Army.SoldierRepository;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WelcomeController {

    private final UserRepository userRepository;

    private final SoldierRepository soldierRepository;

    private final CourierRepository courierRepository;

    private final PostRepository postRepository;

    private final MovementSensorRepository movementSensorRepository;

    @Autowired
    public WelcomeController(UserRepository userRepository, SoldierRepository soldierRepository, CourierRepository courierRepository, PostRepository postRepository, MovementSensorRepository movementSensorRepository) {
        this.userRepository = userRepository;
        this.soldierRepository = soldierRepository;
        this.courierRepository = courierRepository;
        this.postRepository = postRepository;
        this.movementSensorRepository = movementSensorRepository;
    }

    @GetMapping("/")
    public String index(Authentication authentication, Model model) {
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if (user == null)
            return "redirect:/auth/login";

        model.addAttribute("login", user.getLogin());
        model.addAttribute("role", user.getRole());
        model.addAttribute("permissions", user.getRole().getPermissions());
        model.addAttribute("alerts", user.getAlertMessages());
        model.addAttribute("sensors_error", movementSensorRepository.findAllBySensorStatus(SensorStatus.ERROR));

        return "welcome/index";
    }

    @GetMapping("/about_me")
    public String aboutMe(Authentication authentication, Model model) {
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if (user == null)
            return "redirect:/auth/login";

        Soldier soldier = soldierRepository.findByUserId(user.getId()).orElse(null);
        if (soldier != null){

            SoldierUtility soldierUtility = SoldierUtility.toSoldierUtilityOne(soldier);

            model.addAttribute("soldier", soldierUtility);
        }else{
            model.addAttribute("soldier", null);
        }

        Courier courier = courierRepository.findByUserId(user.getId()).orElse(null);
        if(courier != null){
            model.addAttribute("courier", courier);
        }else{
            model.addAttribute("courier", null);
        }

        model.addAttribute("login", user.getLogin());
        model.addAttribute("role", user.getRole());
        model.addAttribute("permissions", user.getRole().getPermissions());

        return "welcome/about_me";
    }
    
}
