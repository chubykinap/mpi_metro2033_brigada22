package b22.metro2033.Controller.Army;

import b22.metro2033.Entity.Alerts.TypeOfMessage;
import b22.metro2033.Entity.Army.*;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.PostUtility;
import b22.metro2033.Entity.Utility.SoldierUtility;
import b22.metro2033.Repository.Army.PostRepository;
import b22.metro2033.Repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    @Autowired
    public PostController(PostRepository postRepository,
                          UserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('army:read')")
    public String index(Model model, Authentication authentication){
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if(user == null){
            return "redirect:/auth/login";
        }

        model.addAttribute("posts", postRepository.findAll());
        return "posts/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('army:write')")
    public String createForm(Model model, Authentication authentication){
        model.addAttribute("post", new Post());
        model.addAttribute("action", "Create");

        return "posts/form";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('army:write')")
    public String create(@ModelAttribute("post") @Valid Post post, BindingResult bindingResult,
                         Model model, Authentication authentication, @RequestParam("action") String action){
        if(bindingResult.hasErrors()) {
            model.addAttribute("action", action);
            model.addAttribute("posts", postRepository.findAll());
            return "posts/form";
        }

        postRepository.save(post);

        return "redirect:/posts";
    }

    /*@GetMapping("/change/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String changeForm(Model model, Authentication authentication, @PathVariable Long id){

        Post post = postRepository.findById(id).orElse(null);
        if(post == null){
            return "redirect:/posts";
        }

        model.addAttribute("post", new PostUtility(post));
        model.addAttribute("action", "change");

        model.addAttribute("solders", post.getSoldier());
        model.addAttribute("sensors", post.getMovementSensors());

        return "army/change";
    }*/

    /*@PreAuthorize("hasAuthority('army:write')")
    @RequestMapping(value = "/change", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String change(@RequestBody String response) throws Exception { //ParesException type?

        JSONObject json = new JSONObject(response);

        long post_id = Long.parseLong(json.getString("post_id"));
        String name = json.getString("name");
        String location = json.getString("location");






        long post_id =  Long.parseLong(json.getString("post_id"));
        int agility =  Integer.parseInt(json.getString("agility"));
        int strength = Integer.parseInt(json.getString("strength"));
        int stamina = Integer.parseInt(json.getString("stamina"));

        //Переделать в 1 запрос (хз как)
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

        soldier.setUser(user);
        if (!soldier.getPost().equals(post)){
            String message = "Вы назначены на новый пост: " + post.getName() + " " + post.getLocation();
            sendAlertMessage(user, message, TypeOfMessage.NOTIFICATION);
        }
        soldier.setPost(post);

        if (!soldier.getRank().equals(rank)){
            String message = "Вам изменили звание: " + rank.toString();
            sendAlertMessage(user, message, TypeOfMessage.NOTIFICATION);
        }
        soldier.setRank(rank);

        if (!soldier.getHealth_state().equals(health_state)){
            String message = "Вам изменили состояние здоровья: " + health_state;
            sendAlertMessage(user, message, TypeOfMessage.NOTIFICATION);
        }
        soldier.setHealth_state(health_state);

        soldierRepository.save(soldier);

        Characteristics characteristics = characteristicsRepository.findBySoldier_id(soldier_id).orElse(null);
        if (characteristics == null){
            return "redirect:/army";
        }

        if (characteristics.getAgility() != agility ||
                characteristics.getStamina() != stamina ||
                characteristics.getStrength() != strength) {
            String message = "Вам изменили характеристики";
            sendAlertMessage(user, message, TypeOfMessage.NOTIFICATION);
        }

        characteristics.setAgility(agility);
        characteristics.setStamina(stamina);
        characteristics.setStrength(strength);

        characteristicsRepository.save(characteristics);

        return "redirect:/army";
    }*/


    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String delete(@PathVariable Long id) {

        Post post = postRepository.findById(id).orElse(null);

        if(post != null){
            postRepository.deleteById(id);
        }

        return "redirect:/posts";
    }


}
