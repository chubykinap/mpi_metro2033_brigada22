package b22.metro2033.Controller;

import b22.metro2033.Entity.User;
import b22.metro2033.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    private final UserRepository userRepository;

    @Autowired
    public WelcomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String index(Authentication authentication, Model model) {
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if (user == null)
            return "redirect:/auth/login";

        model.addAttribute("login", user.getLogin());
        model.addAttribute("role", user.getRole());
        model.addAttribute("permissions", user.getRole().getPermissions());

        return "welcome/index";
    }




}
