package b22.metro2033.Controller.Army;

import b22.metro2033.Entity.Army.MovementSensor;
import b22.metro2033.Entity.Army.Post;
import b22.metro2033.Entity.Army.SensorStatus;
import b22.metro2033.Entity.Army.Soldier;
import b22.metro2033.Repository.Army.MovementSensorRepository;
import b22.metro2033.Repository.Army.PostRepository;
import b22.metro2033.Repository.Army.SoldierRepository;
import b22.metro2033.Repository.UserRepository;
import b22.metro2033.domain.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/posts", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestPostController {

    private final UserRepository userRepository;
    private final SoldierRepository soldierRepository;
    private final PostRepository postRepository;
    private final MovementSensorRepository movementSensorRepository;


    public RestPostController(UserRepository userRepository, SoldierRepository soldierRepository, PostRepository postRepository, MovementSensorRepository movementSensorRepository) {
        this.userRepository = userRepository;
        this.soldierRepository = soldierRepository;
        this.postRepository = postRepository;
        this.movementSensorRepository = movementSensorRepository;
    }

    @PostMapping("/delete_sensor_post")
    @PreAuthorize("hasAuthority('army:write')")
    public Response deleteSensorPost(@RequestBody String request) throws JSONException {

        JSONObject JSON = new JSONObject(request);
        long sensor_id = Long.parseLong(JSON.getString("sensor_id"));

        MovementSensor movementSensor = movementSensorRepository.findById(sensor_id).orElse(null);
        if(movementSensor == null){
            return new Response("Error", "Датчик не найден!");
        }

        movementSensor.setPost(null);
        movementSensor.setSensorStatus(SensorStatus.DEACTIVATED);
        movementSensorRepository.save(movementSensor);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", movementSensor.getName());
        hashMap.put("location", movementSensor.getLocation());

        return new Response("Done", hashMap);
    }

    @PreAuthorize("hasAuthority('army:write')")
    @RequestMapping(value = "/add_sensor_to_post", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response addSensorToPost(@RequestBody String response) throws Exception {

        JSONObject json = new JSONObject(response);

        long post_id = Long.parseLong(json.getString("post_id"));
        long sensor_id = Long.parseLong(json.getString("sensor_id"));

        Post post = postRepository.findById(post_id).orElse(null);
        if(post == null) {
            return new Response("Error", "Пост не найден!");
        }

        MovementSensor movementSensor = movementSensorRepository.findById(sensor_id).orElse(null);
        if(movementSensor == null) {
            return new Response("Error", "Пост не найден!");
        }

        if(movementSensor.getPost() != null){
            if(movementSensor.getPost().getId() == post.getId()){
                return new Response("Error", "Датчик уже назначен на этот пост!");
            }
        }

        movementSensor.setPost(post);
        movementSensor.setSensorStatus(SensorStatus.NORMAL);
        movementSensorRepository.save(movementSensor);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", movementSensor.getName());
        hashMap.put("location", movementSensor.getLocation());

        return new Response("Done", hashMap);
    }

    @PreAuthorize("hasAuthority('army:write')")
    @RequestMapping(value = "/add_soldier_to_post", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response addSoldierToPost(@RequestBody String response) throws Exception {

        JSONObject json = new JSONObject(response);

        long post_id = Long.parseLong(json.getString("post_id"));
        long soldier_id = Long.parseLong(json.getString("soldier_id"));


        Post post = postRepository.findById(post_id).orElse(null);
        if(post == null) {
            return new Response("Error", "Пост не найден!");
        }

        Soldier soldier = soldierRepository.findById(soldier_id).orElse(null);
        if(soldier == null) {
            return new Response("Error", "Солдат не найден!");
        }

        if(soldier.getPost() != null){
            if(soldier.getPost().getId() == post.getId()){
                return new Response("Error", "Солдат уже назначен на этот пост!");
            }
        }

        soldier.setPost(post);
        soldierRepository.save(soldier);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", soldier.getUser().getName());
        hashMap.put("surname", soldier.getUser().getSurname());
        hashMap.put("rank", soldier.getRank().toString());

        return new Response("Done", hashMap);
    }

    @PreAuthorize("hasAuthority('army:write')")
    @RequestMapping(value = "/remove_from_post", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response removeFromPost(@RequestBody String request) throws JSONException {

        JSONObject json = new JSONObject(request);
        long soldier_id = Long.parseLong(json.getString("soldier_id"));

        Soldier soldier = soldierRepository.findById(soldier_id).orElse(null);

        if (soldier == null){
            return new Response("Error", "Солдат не найден!");
        }

        //Нужно ли создавать новый объект?
        Post post = new Post();

        soldier.setPost(null);
        soldierRepository.save(soldier);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", soldier.getUser().getName());
        hashMap.put("surname", soldier.getUser().getSurname());
        hashMap.put("rank", soldier.getRank().toString());

        return new Response("Done", hashMap);
    }

    @PreAuthorize("hasAuthority('army:write')")
    @RequestMapping(value = "/change", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response change(@RequestBody String request) throws Exception {
        JSONObject json = new JSONObject(request);

        long post_id = Long.parseLong(json.getString("post_id"));
        String name = json.getString("name");
        String location = json.getString("location");

        Post post = postRepository.findById(post_id).orElse(null);
        if(post == null) {
            return new Response("Error", "Пост не найден!");
        }

        if (!Objects.equals(name, post.getName())){
            if (postRepository.findByName(name).isPresent()){
                return new Response("Error", "Пост с таким именем уже существует!");
            }
        }

        post.setName(name);
        post.setLocation(location);

        postRepository.save(post);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("location", location);

        return new Response("Done", hashMap);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('army:write')")
    public Response create(@RequestBody String request) throws JSONException {
        JSONObject JSON = new JSONObject(request);
        String name = JSON.getString("name");
        String location = JSON.getString("location");

        if (postRepository.findByName(name).isPresent()){
            return new Response("Error", "Пост с таким именем уже существует!");
        }

        Post post = new Post();
        post.setName(name);
        post.setLocation(location);

        postRepository.save(post);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("location", location);

        return new Response("Done", hashMap);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('army:write')")
    public Response delete(@RequestBody String request) throws JSONException {

        JSONObject JSON = new JSONObject(request);
        Long id = Long.parseLong(JSON.getString("post_id"));

        String name;
        String location;

        Post post = postRepository.findById(id).orElse(null);
        if (post == null){
            return new Response("Error", "Пост не найден!");
        }else{

            name = post.getName();
            location = post.getLocation();

            List<Soldier> soldiers = soldierRepository.findAllByPost(post);
            if (soldiers.size() != 0){
                for (Soldier soldier : soldiers) {
                    soldier.setPost(null);
                }

                soldierRepository.saveAll(soldiers);
            }

            List<MovementSensor> sensors = movementSensorRepository.findAllByPost(post);
            if (sensors.size() != 0){
                for (MovementSensor sensor : sensors) {
                    sensor.setPost(null);
                    sensor.setSensorStatus(SensorStatus.DEACTIVATED);
                }

                movementSensorRepository.saveAll(sensors);
            }

            postRepository.deleteById(id);

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", name);
            hashMap.put("location", location);

            return new Response("Done", hashMap);
        }
    }

}
