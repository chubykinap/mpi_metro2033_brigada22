package b22.metro2033.Controller;

import b22.metro2033.Entity.Army.Characteristics;
import b22.metro2033.Entity.Army.Post;
import b22.metro2033.Entity.Army.Rank;
import b22.metro2033.Entity.Army.Soldier;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.CharacteristicsRepository;
import b22.metro2033.Repository.PostRepository;
import b22.metro2033.Repository.SoldierRepository;
import b22.metro2033.Repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/army")
public class ArmyController {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final SoldierRepository soldierRepository;

    private final CharacteristicsRepository characteristicsRepository;

    @Autowired
    public ArmyController(CharacteristicsRepository characteristicsRepository,
                          SoldierRepository soldierRepository,
                          UserRepository userRepository,
                          PostRepository postRepository){
        this.userRepository = userRepository;
        this.soldierRepository = soldierRepository;
        this.characteristicsRepository = characteristicsRepository;
        this.postRepository = postRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('army:read')")
    public String index(Model model, Authentication authentication){
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if(user == null){
            return "redirect:/auth/login";
        }

        model.addAttribute("soldiers", soldierRepository.findAll());
        return "army/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('army:write')")
    public String createForm(Model model, Authentication authentication){
        model.addAttribute("soldier", new Soldier());
        model.addAttribute("action", "Create");
        List<User> users = userRepository.findAllByRoleIn(getRolesForSelect(authentication));
        if (users.size() == 0){
            return "army/form";
        }
        model.addAttribute("users",  users);
        model.addAttribute("ranks", Rank.values());
        model.addAttribute("posts", postRepository.findAll());

        return "army/form";
    }

    @PreAuthorize("hasAuthority('army:write')")
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String create(@RequestBody String response) throws JSONException, ParseException { //ParesException type?

        JSONObject json = new JSONObject(response);

        String rank_string = json.getString("rank");
        String health_state = json.getString("health_state");
        long user_id = Long.parseLong(json.getString("user_id"));
        long post_id =  Long.parseLong(json.getString("post_id"));
        int agility =  Integer.parseInt(json.getString("agility"));
        int strength = Integer.parseInt(json.getString("strength"));
        int stamina = Integer.parseInt(json.getString("stamina"));

        Soldier soldier = new Soldier();

        User user = userRepository.findById(user_id).orElse(null);
        Post post = postRepository.findById(post_id).orElse(null);
        Rank rank = Rank.valueOf(rank_string);

        soldier.setUser(user);
        soldier.setPost(post);
        soldier.setRank(rank);
        soldier.setHealth_state(health_state);
        soldierRepository.save(soldier);

        Characteristics characteristics = new Characteristics();
        characteristics.setAgility(agility);
        characteristics.setStrength(strength);
        characteristics.setStamina(stamina);
        characteristics.setSoldier(soldier);
        characteristicsRepository.save(characteristics);

        return "redirect:/army";
    }

    @GetMapping("/change/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String changeForm(Model model, Authentication authentication, @PathVariable Long id){

        Soldier soldier = soldierRepository.findById(id).orElse(null);
        if(soldier == null){
            return "redirect:/army";
        }

        model.addAttribute("soldier", soldier);
        model.addAttribute("action", "change");
        List<User> users = userRepository.findAllByRoleIn(getRolesForSelect(authentication));
        if (users.size() == 0){
            return "army/form";
        }
        model.addAttribute("users", users);
        model.addAttribute("ranks", Rank.values());
        model.addAttribute("posts", postRepository.findAll());

        return "army/change";
    }

    @PreAuthorize("hasAuthority('army:write')")
    @RequestMapping(value = "/change", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String change(@RequestBody String response) throws JSONException, ParseException { //ParesException type?

        JSONObject json = new JSONObject(response);

        String rank_string = json.getString("rank");
        String health_state = json.getString("health_state");
        long soldier_id = Long.parseLong(json.getString("soldier_id"));
        long user_id = Long.parseLong(json.getString("user_id"));
        long post_id =  Long.parseLong(json.getString("post_id"));
        int agility =  Integer.parseInt(json.getString("agility"));
        int strength = Integer.parseInt(json.getString("strength"));
        int stamina = Integer.parseInt(json.getString("stamina"));

        System.out.print(stamina);

        Soldier soldier = soldierRepository.findById(soldier_id).orElse(null);
        if(soldier == null) {
            return "redirect:/army";
        }

        User user = userRepository.findById(user_id).orElse(null);
        if(user == null){
            return "redirect:/army";
        }
        //Проверить на пустоту
        Post post = postRepository.findById(post_id).orElse(null);
        Rank rank = Rank.valueOf(rank_string);

        soldier.setUser(user);
        soldier.setPost(post);
        soldier.setRank(rank);
        soldier.setHealth_state(health_state);
        soldierRepository.save(soldier);

        Characteristics characteristics = characteristicsRepository.findBySoldier_id(soldier_id).orElse(null);
        if (characteristics == null){
            return "redirect:/army";
        }

        characteristics.setAgility(agility);
        characteristics.setStamina(stamina);
        characteristics.setStrength(strength);

        characteristicsRepository.save(characteristics);

        return "redirect:/army";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String enable(@PathVariable Long id) {

        Soldier soldier = soldierRepository.findById(id).orElse(null);

        if(soldier != null){
            Characteristics characteristics = characteristicsRepository.findBySoldier_id(id).orElse(null);
            if(characteristics != null){
                characteristicsRepository.delete(characteristics);
            }

            soldierRepository.deleteById(id);
        }

        //soldierRepository.findById(id).ifPresent(soldierRepository::delete);
        return "redirect:/army";
    }

    private List<Role> getRolesForSelect(Authentication authentication){
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
}
