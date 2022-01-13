package b22.metro2033.Controller;

import b22.metro2033.Entity.Army.Rank;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Army.PostRepository;
import b22.metro2033.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final PostRepository postRepository;

    @Autowired
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          PostRepository postRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
    }

    @GetMapping("/login")
    public String login(Principal principal) {
        if (principal != null)
            return "redirect:/";
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model, Principal principal) {
        if (principal != null)
            return "redirect:/";

        model.addAttribute("user", new User());
        model.addAttribute("action", "Create");
        model.addAttribute("roles", getRoles());

        return "auth/register";
    }

    @PostMapping("/register")
    //@RequestBody для тестов
    public String newCustomer(Principal principal, Model model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (principal != null)
            return "redirect:/";

        for (ObjectError error : bindingResult.getAllErrors()) {
            System.out.println(error.toString());
        }
        if (bindingResult.hasErrors())
            return "auth/register";

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //user.setRole(Role.GUEST);

        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            model.addAttribute("error", "Пользователь с таким логином уже существует!");
            return "error";
        }

        userRepository.save(user);

        model.addAttribute("success", true);
        model.addAttribute("user", new User());

        return "auth/register";
    }

    private List<Role> getRoles() {
        List<Role> roles = Arrays.asList(Role.values());
        return roles;
    }
}
