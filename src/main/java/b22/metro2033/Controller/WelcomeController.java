package b22.metro2033.Controller;

import b22.metro2033.Entity.Alerts.AlertMessages;
import b22.metro2033.Entity.Army.MovementSensor;
import b22.metro2033.Entity.Army.SensorStatus;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Army.MovementSensorRepository;
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

    private final MovementSensorRepository movementSensorRepository;

    @Autowired
    public WelcomeController(UserRepository userRepository, MovementSensorRepository movementSensorRepository) {
        this.userRepository = userRepository;
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

        System.out.println("////////////////////////////////////////////////////////////////////");
        List<AlertMessages> alerts = user.getAlertMessages();
        for (int i = 0; i < alerts.size(); i++){
            System.out.println(alerts.get(i).getAlert_message());
        }

        List<MovementSensor> sensorsWithErrors = movementSensorRepository.findAllBySensorStatus(SensorStatus.ERROR);
        for (int i = 0; i < sensorsWithErrors.size(); i++){
            System.out.println(sensorsWithErrors.get(i).getName());
        }

        return "welcome/index";
    }

}
