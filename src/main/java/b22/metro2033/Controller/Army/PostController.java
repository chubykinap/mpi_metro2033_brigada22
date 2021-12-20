package b22.metro2033.Controller.Army;

import b22.metro2033.Entity.Army.*;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.PostUtility;
import b22.metro2033.Entity.Utility.SoldierUtility;
import b22.metro2033.Repository.Army.MovementSensorRepository;
import b22.metro2033.Repository.Army.PostRepository;
import b22.metro2033.Repository.Army.SoldierRepository;
import b22.metro2033.Repository.UserRepository;
import b22.metro2033.Service.PaginatedService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        List<Post> posts = postRepository.findAll();

        Page<Post> postsPage = PaginatedService.findPaginated(PageRequest.of(currentPage - 1, pageSize), posts);

        int totalPages = postsPage.getTotalPages();
        if (totalPages > 0) {

            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = startPage; i < startPage + numberOfPages; i++){
                if (i > totalPages) break;
                pageNumbers.add(i);
            }
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("postsPage", postsPage);
        model.addAttribute("start_page", startPage);
        model.addAttribute("number_of_pages", numberOfPages);

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
    public String showSoldiers(Model model, Authentication authentication, @PathVariable Long id,
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

        Post post = postRepository.findById(id).orElse(null);
        if(post == null){
            return "redirect:/posts";
        }

        List<Soldier> soldiers = post.getSoldier();

        Page<Soldier> soldiersPage = PaginatedService.findPaginated(PageRequest.of(currentPage - 1, pageSize), soldiers);

        model.addAttribute("soldiersPage", soldiersPage);
        model.addAttribute("start_page", startPage);
        model.addAttribute("number_of_pages", numberOfPages);

        int totalPages = soldiersPage.getTotalPages();
        if (totalPages > 0) {

            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = startPage; i < startPage + numberOfPages; i++){
                if (i > totalPages) break;
                pageNumbers.add(i);
            }
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("post", new PostUtility(post));
        model.addAttribute("action", "change");


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
    public String showSensorsForm(Model model, Authentication authentication, @PathVariable Long id,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size,
                                  @RequestParam("start_page") Optional<Integer> start_page,
                                  @RequestParam("number_of_pages") Optional<Integer> number_of_pages){

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(1);
        int startPage = start_page.orElse(1);
        int numberOfPages = number_of_pages.orElse(1);

        if (startPage < 0) startPage = 1;
        if (currentPage < 0) currentPage = 1;

        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if(user == null){
            return "redirect:/auth/login";
        }

        List<MovementSensor> sensors = movementSensorRepository.findAllByPostId(id);

        Page<MovementSensor> sensorsPage = PaginatedService.findPaginated(PageRequest.of(currentPage - 1, pageSize), sensors);

        model.addAttribute("sensorsPage", sensorsPage);
        model.addAttribute("start_page", startPage);
        model.addAttribute("number_of_pages", numberOfPages);

        int totalPages = sensorsPage.getTotalPages();
        if (totalPages > 0) {

            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = startPage; i < startPage + numberOfPages; i++){
                if (i > totalPages) break;
                pageNumbers.add(i);
            }
            model.addAttribute("pageNumbers", pageNumbers);
        }

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
