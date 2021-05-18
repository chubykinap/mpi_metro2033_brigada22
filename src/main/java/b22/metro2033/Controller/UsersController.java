package b22.metro2033.Controller;

import b22.metro2033.Entity.Army.Characteristics;
import b22.metro2033.Entity.Army.Soldier;
import b22.metro2033.Entity.Permission;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersController(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('users:read')")
    public String index(Model model, Authentication authentication){
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if(user == null){
            return "redirect:/auth/login";
        }

        List<Role> roles = new ArrayList<>();

        switch (user.getRole()){
            case ADMIN:
                roles = Stream.of(Role.ADMIN, Role.GENERAL, Role.SOLDIER,
                                    Role.HEAD_ENGINEER, Role.ENGINEER,
                                    Role.HEAD_COURIER, Role.COURIER).collect(Collectors.toList());
                break;
        }

        model.addAttribute("users", userRepository.findAllByRoleIn(roles));
        return "users/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('users:create')")
    public String createForm(Model model, Authentication authentication){
        model.addAttribute("user", new User());
        model.addAttribute("action", "Create");
        model.addAttribute("roles", getRolesForSelect(authentication));

        return "users/form";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('users:create')")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                        Model model, Authentication authentication,  @RequestParam("action") String action){
        if(bindingResult.hasErrors()) {
            model.addAttribute("action", action);
            model.addAttribute("roles", getRolesForSelect(authentication));
            return "users/form";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String enable(@PathVariable Long id) {

        User user = userRepository.findById(id).orElse(null);

        if(user != null){
            userRepository.deleteById(id);
        }

        return "redirect:/users";
    }

    private List<Role> getRolesForSelect(Authentication authentication){
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);

        List<Role> roles = new ArrayList<>();

        if(user == null){
            return roles;
        }

        switch (user.getRole()){
            case ADMIN:
                roles = Stream.of(Role.ADMIN, Role.GENERAL, Role.SOLDIER,
                                    Role.HEAD_COURIER, Role.HEAD_COURIER,
                                    Role.HEAD_ENGINEER, Role.HEAD_ENGINEER).collect(Collectors.toList());
                break;
        }

        return roles;
    }

}
