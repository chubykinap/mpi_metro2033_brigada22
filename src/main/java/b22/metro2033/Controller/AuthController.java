package b22.metro2033.Controller;

import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Army.PostRepository;
import b22.metro2033.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        if(principal != null)
            return "redirect:/";
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model, Principal principal) {
        if(principal != null)
            return "redirect:/";

        model.addAttribute("user", new User());
        model.addAttribute("action", "Create");
        model.addAttribute("roles", getRoles());

        /*Post post = new Post();
        post.setLocation("Moscow Station");
        post.setName("MSK");

        postRepository.save(post);*/

        return "auth/register";
    }

    @PostMapping("/register")
    public String newCustomer(Principal principal, Model model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if(principal != null)
            return "redirect:/";

        if(bindingResult.hasErrors())
            return "auth/register";

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //user.setRole(Role.GUEST);
        userRepository.save(user);

        model.addAttribute("success", true);
        model.addAttribute("user", new User());

        return "auth/register";
    }

    private List<Role> getRoles(){
        List<Role> roles = Arrays.asList(Role.values());
        return roles;
    }
}
