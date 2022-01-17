package b22.metro2033.Controller.Army;

import b22.metro2033.Entity.Army.*;
import b22.metro2033.Entity.Role;
import b22.metro2033.Repository.Army.MovementSensorRepository;
import b22.metro2033.Repository.Army.PostRepository;
import b22.metro2033.Repository.Army.SensorMessagesRepository;
import b22.metro2033.Repository.UserRepository;
import b22.metro2033.Service.PaginatedService;
import b22.metro2033.Service.SensorService;
import b22.metro2033.domain.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "/sensors", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestSensorController {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final MovementSensorRepository movementSensorRepository;

    private final SensorMessagesRepository sensorMessagesRepository;

    @Autowired
    private SensorService sensorService;

    public RestSensorController(UserRepository userRepository,
                                PostRepository postRepository,
                                MovementSensorRepository movementSensorRepository,
                                SensorMessagesRepository sensorMessagesRepository,
                                SensorService sensorService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.movementSensorRepository = movementSensorRepository;
        this.sensorMessagesRepository = sensorMessagesRepository;
        this.sensorService = sensorService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('army:write')")
    public Response create(@RequestBody String request) throws JSONException {
        JSONObject JSON = new JSONObject(request);

        String name = JSON.getString("name");
        String location = JSON.getString("location");
        Long post_id = Long.parseLong(JSON.getString("post_id"));

        if (movementSensorRepository.findByName(name).isPresent()){
            return new Response("Error", "Датчик с таким именем уже существует!");
        }

        MovementSensor movementSensor = new MovementSensor();
        movementSensor.setName(name);
        movementSensor.setSensorStatus(SensorStatus.NORMAL);
        movementSensor.setLocation(location);

        Post post = postRepository.findById(post_id).orElse(null);
        if (post == null){
            return new Response("Error", "Пост не найден!");
        }

        movementSensor.setPost(post);

        movementSensorRepository.save(movementSensor);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("location", location);

        return new Response("Done", hashMap);
    }

    @PreAuthorize("hasAuthority('army:write')")
    @RequestMapping(value = "/change", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response change(@RequestBody String request) throws Exception {
        JSONObject JSON = new JSONObject(request);

        String name = JSON.getString("name");
        String location = JSON.getString("location");
        Long post_id = Long.parseLong(JSON.getString("post_id"));
        Long sensor_id = Long.parseLong(JSON.getString("sensor_id"));

        MovementSensor movementSensor = movementSensorRepository.findById(sensor_id).orElse(null);
        if (movementSensor == null){
            return new Response("Error", "Датчик не найден!");
        }

        if (!Objects.equals(name, movementSensor.getName())){
            if (movementSensorRepository.findByName(name).isPresent()){
                return new Response("Error", "Датчик с таким именем уже существует!");
            }
        }

        if(movementSensor.getPost().getId() != movementSensor.getPost().getId()){
            movementSensor.setSensorStatus(SensorStatus.NORMAL);
        }

        movementSensor.setPost(movementSensor.getPost());
        movementSensor.setLocation(movementSensor.getLocation());
        movementSensor.setName(movementSensor.getName());

        movementSensorRepository.save(movementSensor);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("location", location);

        return new Response("Done", hashMap);
    }

    @PostMapping("/done")
    @PreAuthorize("hasAuthority('army:write')")
    public Response problemDone(@RequestBody String request) throws JSONException {

        JSONObject JSON = new JSONObject(request);
        Long id = Long.parseLong(JSON.getString("sensor_id"));

        MovementSensor movementSensor = movementSensorRepository.findById(id).orElse(null);
        if(movementSensor == null){
            return new Response("Error", "Датчкик не найден!");
        }

        movementSensor.setSensorStatus(SensorStatus.NORMAL);
        movementSensorRepository.save(movementSensor);

        return new Response("Done", "Проблема решена");
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('army:write')")
    public Response delete(@RequestBody String request) throws JSONException {
        JSONObject JSON = new JSONObject(request);
        Long id = Long.parseLong(JSON.getString("sensor_id"));

        MovementSensor movementSensor = movementSensorRepository.findById(id).orElse(null);

        String name;
        String location;

        if(movementSensor != null) {
            name = movementSensor.getName();
            location = movementSensor.getLocation();

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", name);
            hashMap.put("location", location);


            movementSensorRepository.deleteById(id);

            return new Response("Done", hashMap);

        }else{
            return new Response("Error", "Датчик не найден");
        }
    }

    @PostMapping("/messages")
    @PreAuthorize("hasAuthority('army:read')")
    public String messages(Model model, Authentication authentication, @PathVariable Long id,
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


        MovementSensor movementSensor = movementSensorRepository.findById(id).orElse(null);
        if(movementSensor == null){
            return "redirect:/sensors";
        }

        List<SensorMessages> sensorMessages = sensorMessagesRepository.findByMovementSensorOrderByIdDesc(movementSensor);

        Page<SensorMessages> messagesPage = PaginatedService.findPaginated(PageRequest.of(currentPage - 1, pageSize), sensorMessages);

        model.addAttribute("messagesPage", messagesPage);
        model.addAttribute("start_page", startPage);
        model.addAttribute("number_of_pages", numberOfPages);

        int totalPages = messagesPage.getTotalPages();
        if (totalPages > 0) {

            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = startPage; i < startPage + numberOfPages; i++){
                if (i > totalPages) break;
                pageNumbers.add(i);
            }
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("sensor", movementSensor);
        model.addAttribute("action", "change");
        List<Post> posts = postRepository.findAll();
        if (posts.size() == 0){
            return "sensors/form";
        }

        model.addAttribute("posts", posts);

        return "sensors/messages";
    }


}
