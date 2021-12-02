package b22.metro2033.Controller.Engineering;

import b22.metro2033.Entity.Army.*;
import b22.metro2033.Entity.Engineering.Engineer;
import b22.metro2033.Entity.Engineering.Qualification;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.EngineerUtility;
import b22.metro2033.Entity.Utility.SoldierUtility;
import b22.metro2033.Repository.Engineering.EngineerRepository;
import b22.metro2033.Repository.Engineering.RequestRepository;
import b22.metro2033.Repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/engineering")
public class EngineerController {
    private final EngineerRepository engineerRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Autowired
    public EngineerController(EngineerRepository engineerRepository, RequestRepository requestRepository, UserRepository userRepository) {
        this.engineerRepository = engineerRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('engineering:read')")
    public String index(Model model, Authentication authentication){
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if(user == null){
            return "redirect:/auth/login";
        }
        List<Engineer> engineers = engineerRepository.findAll();
        model.addAttribute("engineers", EngineerUtility.toEngineerUtility(engineers));

        return "engineering/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('engineering:write')")
    public String createForm(Model model, Authentication authentication){
        model.addAttribute("engineering", new Engineer());
        model.addAttribute("action", "Create");
        List<User> users = userRepository.findAllByRoleIn(getRolesForSelect(authentication));
        if (users.size() == 0){
            return "engineering/form";
        }
        model.addAttribute("users",  users);
        model.addAttribute("qualifications", Rank.getRankListRU());

        return "engineering/form";
    }

    private List<Role> getRolesForSelect(Authentication authentication) {
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);

        List<Role> roles = new ArrayList<>();

        if(user == null){
            return roles;
        }

        switch (user.getRole()){
            case GENERAL:

            case ADMIN:
                roles = Stream.of(Role.GENERAL, Role.SOLDIER).collect(Collectors.toList());
                break;
        }

        return roles;
    }

    @PreAuthorize("hasAuthority('engineering:write')")
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String create(@RequestBody @Valid String response) throws Exception { //ParesException type?

        JSONObject json = new JSONObject(response);

        Qualification qualification = Qualification.findState(json.getString("qualification"));
        long user_id = Long.parseLong(json.getString("user_id"));

        Engineer engineer = new Engineer();

        User user = userRepository.findById(user_id).orElse(null);


        engineer.setUser(user);

        return "redirect:/engineering";
    }

}
