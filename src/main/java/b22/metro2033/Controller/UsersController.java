package b22.metro2033.Controller;

import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.UserUtility;
import b22.metro2033.Repository.UserRepository;
import b22.metro2033.Service.PaginatedService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
    public String index(Model model, Authentication authentication,
    @RequestParam("page") Optional<Integer> page,
    @RequestParam("size") Optional<Integer> size,
                        @RequestParam("start_page") Optional<Integer> start_page,
                        @RequestParam("number_of_pages") Optional<Integer> number_of_pages){

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        int startPage = start_page.orElse(1);
        int numberOfPages = number_of_pages.orElse(10);

        if (startPage < 0) startPage = 1;
        if (currentPage < 0) currentPage = 1;

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

        List<User> users = userRepository.findAllByRoleIn(roles);

        Page<User> bookPage = PaginatedService.findPaginated(PageRequest.of(currentPage - 1, pageSize), users);

        model.addAttribute("usersPage", bookPage);
        model.addAttribute("start_page", startPage);
        model.addAttribute("number_of_pages", numberOfPages);

        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {

            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = startPage; i < startPage + numberOfPages; i++){
                if (i > totalPages) break;
                pageNumbers.add(i);
            }
            model.addAttribute("pageNumbers", pageNumbers);
        }

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

        if (userRepository.findByLogin(user.getLogin()).isPresent()){
            model.addAttribute("error", "Пользователь с таким логином уже существует!");
            return "error";
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

    @GetMapping("/change/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public String changeForm(Model model, Authentication authentication, @PathVariable Long id){

        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            return "redirect:/users";
        }

        model.addAttribute("user", new UserUtility(user));
        model.addAttribute("action", "change");
        List<User> users = userRepository.findAllByRoleIn(getRolesForSelect(authentication));
        if (users.size() == 0){
            return "army/form";
        }

        model.addAttribute("roles", getRoles());

        return "users/change";
    }

    @PreAuthorize("hasAuthority('users:write')")
    @RequestMapping(value = "/change", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String change(@RequestBody String response) throws Exception { //ParesException type?

        JSONObject json = new JSONObject(response);

        boolean change_pass = true;

        long user_id = Long.parseLong(json.getString("user_id"));
        String login = json.getString("login");

        String password = json.getString("password");
        if (password.equals("")) change_pass = false;

        String encoded_password = passwordEncoder.encode(password);
        String name = json.getString("name");
        String surname = json.getString("surname");
        String patronymic = json.getString("patronymic");
        Role role = Role.findState(json.getString("role"));

        //Переделать в 1 запрос (хз как)
        User user = userRepository.findById(user_id).orElse(null);
        if(user == null) {
            return "redirect:/users";
        }

        if(change_pass){
            user.setPassword(encoded_password);
        }

        user.setLogin(login);
        user.setName(name);
        user.setSurname(surname);
        user.setPatronymic(patronymic);
        user.setRole(role);

        userRepository.save(user);

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
                                    Role.HEAD_COURIER, Role.COURIER,
                                    Role.HEAD_ENGINEER, Role.ENGINEER).collect(Collectors.toList());
                break;
        }

        return roles;
    }

    private List<Role> getRoles(){
        List<Role> roles = Arrays.asList(Role.values());
        return roles;
    }

}
