package b22.metro2033.Controller.Army;

import b22.metro2033.Entity.Army.*;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.PostUtility;
import b22.metro2033.Repository.Army.MovementSensorRepository;
import b22.metro2033.Repository.Army.PostRepository;
import b22.metro2033.Repository.Army.SoldierRepository;
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
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final UserRepository userRepository;
    private final SoldierRepository soldierRepository;
    private final PostRepository postRepository;
    private final MovementSensorRepository movementSensorRepository;

    @Autowired
    public PostController(PostRepository postRepository,
                          UserRepository userRepository, SoldierRepository soldierRepository, MovementSensorRepository movementSensorRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.soldierRepository = soldierRepository;
        this.movementSensorRepository = movementSensorRepository;
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

    @GetMapping("/show_soldiers/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String showSoldiers(Model model, Authentication authentication, @PathVariable Long id){

        Post post = postRepository.findById(id).orElse(null);
        if(post == null){
            return "redirect:/posts";
        }

        model.addAttribute("post", new PostUtility(post));
        model.addAttribute("action", "change");
        model.addAttribute("soldiers", post.getSoldier());

        //model.addAttribute("sensors", post.getMovementSensors());

        return "posts/show_soldiers";
    }

    @GetMapping("/change/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String changeForm(Model model, Authentication authentication, @PathVariable Long id){

        Post post = postRepository.findById(id).orElse(null);
        if(post == null){
            return "redirect:/posts";
        }

        model.addAttribute("post", new PostUtility(post));
        model.addAttribute("action", "change");

        //model.addAttribute("solders", post.getSoldier());
        //model.addAttribute("sensors", post.getMovementSensors());

        return "posts/change";
    }

    @PreAuthorize("hasAuthority('army:write')")
    @RequestMapping(value = "/change", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String change(@RequestBody String response) throws Exception { //ParesException type?

        JSONObject json = new JSONObject(response);

        long post_id = Long.parseLong(json.getString("post_id"));
        String name = json.getString("name");
        String location = json.getString("location");

        //Переделать в 1 запрос (хз как)
        Post post = postRepository.findById(post_id).orElse(null);
        if(post == null) {
            return "redirect:/posts";
        }

        post.setName(name);
        post.setLocation(location);

        postRepository.save(post);

        return "redirect:/posts";
    }

    @GetMapping("/add_soldier_to_post/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String addSoldierToPostForm(Model model, Authentication authentication, @PathVariable Long id){

        Post post = postRepository.findById(id).orElse(null);
        if(post == null){
            return "redirect:/posts";
        }

        model.addAttribute("post", new PostUtility(post));
        model.addAttribute("action", "change");
        List<Soldier> soldierList = soldierRepository.findAllByPostIsNull();
        if(soldierList.isEmpty()){
            model.addAttribute("soldiers","NoData");
        }else{
            model.addAttribute("soldiers", soldierList);
        }

        return "posts/add_soldier_to_post";
    }

    @GetMapping("/add_sensor_to_post/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String addSensorToPostForm(Model model, Authentication authentication, @PathVariable Long id){

        Post post = postRepository.findById(id).orElse(null);
        if(post == null){
            return "redirect:/posts";
        }

        model.addAttribute("post", new PostUtility(post));
        model.addAttribute("action", "change");
        List<MovementSensor> movementSensors = movementSensorRepository.findAllByPostIsNull();
        if(movementSensors.isEmpty()){
            model.addAttribute("sensors","NoData");
        }else{
            model.addAttribute("sensors", movementSensors);
        }

        return "posts/add_sensor_to_post";
    }

    @PreAuthorize("hasAuthority('army:write')")
    @RequestMapping(value = "/add_sensor_to_post", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String addSensorToPost(@RequestBody String response) throws Exception { //ParesException type?

        JSONObject json = new JSONObject(response);

        long post_id = Long.parseLong(json.getString("post_id"));
        long sensor_id = Long.parseLong(json.getString("sensor_id"));

        //Переделать в 1 запрос (хз как)
        Post post = postRepository.findById(post_id).orElse(null);
        if(post == null) {
            return "redirect:/posts/show_sensors/" + post_id;
        }

        MovementSensor movementSensor = movementSensorRepository.findById(sensor_id).orElse(null);
        if(movementSensor == null) {
            return "redirect:/posts/show_sensors/" + post_id;
        }

        movementSensor.setPost(post);
        movementSensorRepository.save(movementSensor);

        return "redirect:/posts/show_sensors/" + post_id;
    }

    @GetMapping("/show_sensors/{id}")
    @PreAuthorize("hasAuthority('army:read')")
    public String showSensorsForm(Model model, Authentication authentication, @PathVariable Long id){
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if(user == null){
            return "redirect:/auth/login";
        }

        model.addAttribute("sensors", movementSensorRepository.findAllByPostId(id));
        Post post = postRepository.findById(id).orElse(null);
        if(post == null) {
            model.addAttribute("post", "NoData");
            return "posts/show_sensors";
        }
        model.addAttribute("post", post);

        return "posts/show_sensors";
    }

    @GetMapping("/show_sensor_messages/{id}")
    @PreAuthorize("hasAuthority('army:read')")
    public String showSensorMessages(Model model, Authentication authentication, @PathVariable Long id){

        MovementSensor movementSensor = movementSensorRepository.findById(id).orElse(null);
        if(movementSensor == null){
            return "redirect:/posts";
        }

        model.addAttribute("sensor", movementSensor);
        model.addAttribute("action", "change");
        Post post = movementSensor.getPost();
        if (post == null){
            model.addAttribute("post", "NoData");
            return "posts/show_sensor_messages";
        }

        model.addAttribute("post", post);

        return "posts/show_sensor_messages";
    }

    @GetMapping("/done/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String problemDone(@PathVariable Long id) {

        MovementSensor movementSensor = movementSensorRepository.findById(id).orElse(null);
        if(movementSensor == null){
            return "redirect:/posts";
        }

        movementSensor.setSensorStatus(SensorStatus.NORMAL);
        movementSensorRepository.save(movementSensor);

        return "redirect:/posts/show_sensors/" + movementSensor.getPost().getId();
    }

    @GetMapping("/delete_sensor_post/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String deleteSensorPost(@PathVariable Long id) {
        MovementSensor movementSensor = movementSensorRepository.findById(id).orElse(null);
        if(movementSensor == null){
            return "redirect:/posts";
        }

        long post_id = movementSensor.getPost().getId();
        movementSensor.setPost(null);
        movementSensor.setSensorStatus(SensorStatus.DEACTIVATED);
        movementSensorRepository.save(movementSensor);

        return "redirect:/posts/show_sensors/" + post_id;
    }

    @PreAuthorize("hasAuthority('army:write')")
    @RequestMapping(value = "/add_soldier_to_post", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String addSoldierToPost(@RequestBody String response) throws Exception { //ParesException type?

        JSONObject json = new JSONObject(response);

        long post_id = Long.parseLong(json.getString("post_id"));
        long soldier_id = Long.parseLong(json.getString("soldier_id"));

        //Переделать в 1 запрос (хз как)
        Post post = postRepository.findById(post_id).orElse(null);
        if(post == null) {
            return "redirect:/posts";
        }

        Soldier soldier = soldierRepository.findById(soldier_id).orElse(null);
        if(soldier == null) {
            return "redirect:/posts";
        }

        soldier.setPost(post);
        soldierRepository.save(soldier);

        return "redirect:/posts";
    }


    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String delete(@PathVariable Long id) {

        Post post = postRepository.findById(id).orElse(null);

        if(post != null){
            postRepository.deleteById(id);
        }

        return "redirect:/posts";
    }

    @GetMapping("/remove_from_post/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String removeFromPost(@PathVariable Long id) {

        Soldier soldier = soldierRepository.findById(id).orElse(null);

        //Нужно ли создавать новый объект?
        Post post = new Post();

        if(soldier != null){
            soldier.setPost(null);
            soldierRepository.save(soldier);
        }

        return "redirect:/posts";
    }

}
