package b22.metro2033.Controller.Army;

import b22.metro2033.Entity.Alerts.AlertMessages;
import b22.metro2033.Entity.Alerts.TypeOfMessage;
import b22.metro2033.Entity.Army.*;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Alerts.AlertsRepository;
import b22.metro2033.Repository.Army.CharacteristicsRepository;
import b22.metro2033.Repository.Army.PostRepository;
import b22.metro2033.Repository.Army.SoldierRepository;
import b22.metro2033.Repository.UserRepository;
import b22.metro2033.domain.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/army", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestArmyController {
    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final SoldierRepository soldierRepository;

    private final CharacteristicsRepository characteristicsRepository;

    private final AlertsRepository alertsRepository;


    public RestArmyController(UserRepository userRepository,
                              PostRepository postRepository,
                              SoldierRepository soldierRepository,
                              CharacteristicsRepository characteristicsRepository,
                              AlertsRepository alertsRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.soldierRepository = soldierRepository;
        this.characteristicsRepository = characteristicsRepository;
        this.alertsRepository = alertsRepository;
    }

    @PreAuthorize("hasAuthority('army:write')")
    @RequestMapping(value = "/change", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response change(@RequestBody String response) throws Exception {

        JSONObject json = new JSONObject(response);

        Rank rank = Rank.findState(json.getString("rank"));
        HealthState health_state = HealthState.findState(json.getString("health_state"));
        long soldier_id = Long.parseLong(json.getString("soldier_id"));
        long user_id = Long.parseLong(json.getString("user_id"));
        long post_id = Long.parseLong(json.getString("post_id"));
        int agility = Integer.parseInt(json.getString("agility"));
        int strength = Integer.parseInt(json.getString("strength"));
        int stamina = Integer.parseInt(json.getString("stamina"));

        System.out.print(stamina);

        //???????????????????? ?? 1 ???????????? (???? ??????)
        Soldier soldier = soldierRepository.findById(soldier_id).orElse(null);
        if (soldier == null) {
            return new Response("Error", "???????????? ???? ????????????!");
        }

        User user = userRepository.findById(user_id).orElse(null);
        if (user == null) {
            return new Response("Error", "???????????????????????? ???? ????????????!");
        }

        soldier.setUser(user);

        //?????????????????? ???? ??????????????
        if (post_id == -1){
            soldier.setPost(null);
            String message = "?????? ?????????? ?? ??????????";
            sendAlertMessage(user, message, TypeOfMessage.NOTIFICATION);
        }else{
            Post post = postRepository.findById(post_id).orElse(null);

            if (post != null && soldier.getPost() != null){
                if (!soldier.getPost().equals(post)){
                    String message = "???? ?????????????????? ???? ?????????? ????????: " + post.getName() + " " + post.getLocation();
                    sendAlertMessage(user, message, TypeOfMessage.NOTIFICATION);
                }
                soldier.setPost(post);
            }else if(post != null && soldier.getPost() == null) {
                String message = "???? ?????????????????? ???? ?????????? ????????: " + post.getName() + " " + post.getLocation();
                sendAlertMessage(user, message, TypeOfMessage.NOTIFICATION);
                soldier.setPost(post);
            }
        }

        if (!soldier.getRank().equals(rank)){
            String message = "?????? ???????????????? ????????????: " + rank.toString();
            sendAlertMessage(user, message, TypeOfMessage.NOTIFICATION);
        }
        soldier.setRank(rank);

        if (!soldier.getHealth_state().equals(health_state)){
            String message = "?????? ???????????????? ?????????????????? ????????????????: " + health_state;
            sendAlertMessage(user, message, TypeOfMessage.NOTIFICATION);
        }
        soldier.setHealth_state(health_state);

        soldierRepository.save(soldier);

        Characteristics characteristics = characteristicsRepository.findBySoldier_id(soldier_id).orElse(null);
        if (characteristics == null){
            return new Response("Error", "???????????????????????????? ?????????????? ???? ??????????????");
        }

        if (characteristics.getAgility() != agility ||
                characteristics.getStamina() != stamina ||
                characteristics.getStrength() != strength) {
            String message = "?????? ???????????????? ????????????????????????????";
            sendAlertMessage(user, message, TypeOfMessage.NOTIFICATION);
        }

        characteristics.setAgility(agility);
        characteristics.setStamina(stamina);
        characteristics.setStrength(strength);
        characteristicsRepository.save(characteristics);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("login", user.getLogin());
        hashMap.put("name", user.getName());
        hashMap.put("surname", user.getSurname());
        hashMap.put("rank", soldier.getRank().toString());

        return new Response("Done", hashMap);
    }

    //TODO ?????????????? ???????????????? ???????????????????????? ???? ???????????? ?????????? ????????????????????
    @PreAuthorize("hasAuthority('army:write')")
    @RequestMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response createSoldier(@RequestBody String response) throws Exception {

        JSONObject json = new JSONObject(response);

        Rank rank = Rank.findState(json.getString("rank"));
        HealthState health_state = HealthState.findState(json.getString("health_state"));
        long user_id = Long.parseLong(json.getString("user_id"));
        long post_id = Long.parseLong(json.getString("post_id"));
        int agility = Integer.parseInt(json.getString("agility"));
        int strength = Integer.parseInt(json.getString("strength"));
        int stamina = Integer.parseInt(json.getString("stamina"));

        Soldier soldier = new Soldier();

        User user = userRepository.findById(user_id).orElse(null);
        if (user == null){
            return new Response("Error", "???????????????????????? ???? ????????????!");
        }

        if (user.getSoldier() != null ||
                user.getCourier() != null){
            return new Response("Error", "???????????????????????? ?????? ????????????????!");
        }

        //TODO ???????????????????
        Post post = postRepository.findById(post_id).orElse(null);

        Characteristics characteristics = new Characteristics(agility, strength, stamina);

        soldier.setUser(user);
        soldier.setPost(post);
        soldier.setRank(rank);
        soldier.setHealth_state(health_state);
        soldierRepository.save(soldier);

        characteristics.setSoldier(soldier);
        characteristicsRepository.save(characteristics);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("login", user.getLogin());
        hashMap.put("name", user.getName());
        hashMap.put("surname", user.getSurname());
        hashMap.put("rank", soldier.getRank().toString());

        return new Response("Done", hashMap);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('army:write')")
    public Response deleteUser(@RequestBody String request) throws IOException, JSONException {

        JSONObject JSON = new JSONObject(request);
        Long id = Long.parseLong(JSON.getString("soldier_id"));

        Soldier soldier = soldierRepository.findById(id).orElse(null);
        String login;
        String name;
        String surname;
        String rank;

        if(soldier != null){
            login = soldier.getUser().getLogin();
            name = soldier.getUser().getName();
            surname = soldier.getUser().getSurname();
            rank = soldier.getRank().toString();

            Characteristics characteristics = characteristicsRepository.findBySoldier_id(id).orElse(null);
            if(characteristics != null){
                characteristicsRepository.delete(characteristics);
            }

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("login", login);
            hashMap.put("name", name);
            hashMap.put("surname", surname);
            hashMap.put("rank", rank);

            soldierRepository.deleteById(id);

            return new Response("Done", hashMap);

        }else{
            return new Response("Error", "???????????????????????? ???? ????????????");
        }
    }

    public void sendAlertMessage(User user, String message, TypeOfMessage type){
        AlertMessages alertMessages = new AlertMessages();
        alertMessages.setUser(user);
        alertMessages.setAlert_message(message);
        alertMessages.setType_of_message(type);

        alertsRepository.save(alertMessages);
    }

}
