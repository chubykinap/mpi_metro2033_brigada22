package b22.metro2033.Controller.Army;

import b22.metro2033.Entity.Army.*;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Army.MovementSensorRepository;
import b22.metro2033.Repository.Army.PostRepository;
import b22.metro2033.Repository.Army.SensorMessagesRepository;
import b22.metro2033.Repository.UserRepository;
import b22.metro2033.Service.PaginatedService;
import b22.metro2033.Service.SensorService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping("/sensors")
public class SensorController {
    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final MovementSensorRepository movementSensorRepository;

    private final SensorMessagesRepository sensorMessagesRepository;

    @Autowired
    private SensorService sensorService;

    @Autowired
    public SensorController(PostRepository postRepository,
                            UserRepository userRepository,
                            MovementSensorRepository movementSensorRepository,
                            SensorMessagesRepository sensorMessagesRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.movementSensorRepository = movementSensorRepository;
        this.sensorMessagesRepository = sensorMessagesRepository;
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

        List<MovementSensor> sensors = movementSensorRepository.findAll();

        Page<MovementSensor> sensorsPage = PaginatedService.findPaginated(PageRequest.of(currentPage - 1, pageSize), sensors);

        model.addAttribute("sensorsPage", sensorsPage);
        model.addAttribute("start_page", startPage);
        model.addAttribute("number_of_pages", numberOfPages);
        model.addAttribute("current_page", currentPage);

        int totalPages = sensorsPage.getTotalPages();
        if (totalPages > 0) {

            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = startPage; i < startPage + numberOfPages; i++){
                if (i > totalPages) break;
                pageNumbers.add(i);
            }
            model.addAttribute("pageNumbers", pageNumbers);
        }


        model.addAttribute("sensors", movementSensorRepository.findAll());
        createMessage();
        return "sensors/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('army:write')")
    public String createForm(Model model, Authentication authentication){
        model.addAttribute("sensor", new MovementSensor());
        model.addAttribute("action", "Create");
        List<Post> posts = postRepository.findAll();
        if (posts.size() == 0){
            return "sensors/form";
        }

        model.addAttribute("posts", posts);

        return "sensors/form";
    }

    @GetMapping("/sensors_table")
    @PreAuthorize("hasAuthority('army:read')")
    public ModelAndView getSensors() {

        List<MovementSensor> sensors = movementSensorRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("sensors/index::sensors_table");
        modelAndView.addObject("sensors", sensors);
        return modelAndView;
    }

//    @PostMapping
//    @PreAuthorize("hasAuthority('army:write')")
//    public String create(@ModelAttribute("sensor") @Valid MovementSensor movementSensor, BindingResult bindingResult,
//                         Model model, Authentication authentication, @RequestParam("action") String action){
//        if(bindingResult.hasErrors()) {
//            model.addAttribute("action", action);
//            model.addAttribute("sensors", movementSensorRepository.findAll());
//            return "sensors/form";
//        }
//        movementSensor.setSensorStatus(SensorStatus.NORMAL);
//        movementSensorRepository.save(movementSensor);
//
//        return "redirect:/sensors";
//    }

//    @GetMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('army:write')")
//    public String delete(@PathVariable Long id) {
//        movementSensorRepository.findById(id).ifPresent(movementSensor -> movementSensorRepository.deleteById(id));
//        return "redirect:/sensors";
//    }
//
    @GetMapping("/change/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String changeForm(Model model, Authentication authentication, @PathVariable Long id){

        MovementSensor movementSensor = movementSensorRepository.findById(id).orElse(null);
        if(movementSensor == null){
            return "redirect:/sensors";
        }

        model.addAttribute("sensor", movementSensor);
        model.addAttribute("action", "change");
        List<Post> posts = postRepository.findAll();
        if (posts.size() == 0){
            return "sensors/form";
        }
        model.addAttribute("posts", posts);

        return "sensors/change";
    }

//    @PreAuthorize("hasAuthority('army:write')")
//    @RequestMapping(value = "/change", method = RequestMethod.POST)
//    public String change(@ModelAttribute("sensor") @Valid MovementSensor movementSensor, BindingResult bindingResult,
//                         Model model, Authentication authentication, @RequestParam("action") String action){
//        if(bindingResult.hasErrors()) {
//            model.addAttribute("action", action);
//            model.addAttribute("sensors", movementSensorRepository.findAll());
//            return "sensors/form";
//        }
//
//        //Переделать в 1 запрос
//        MovementSensor change_sensor = movementSensorRepository.findById(movementSensor.getId()).orElse(null);
//
//        if(change_sensor == null){
//            return "redirect:/sensors";
//        }
//
//        if (movementSensor.getPost() != null){
//
//            if(change_sensor.getPost().getId() != movementSensor.getPost().getId()){
//                change_sensor.setSensorStatus(SensorStatus.NORMAL);
//            }
//        }
//
//        change_sensor.setPost(movementSensor.getPost());
//        change_sensor.setLocation(movementSensor.getLocation());
//        change_sensor.setName(movementSensor.getName());
//
//        movementSensorRepository.save(change_sensor);
//
//        return "redirect:/sensors";
//    }

    @GetMapping("/messages/{id}")
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
        model.addAttribute("current_page", currentPage);

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

//    @GetMapping("/done/{id}")
//    @PreAuthorize("hasAuthority('army:write')")
//    public String problemDone(@PathVariable Long id) {
//
//        MovementSensor movementSensor = movementSensorRepository.findById(id).orElse(null);
//        if(movementSensor == null){
//            return "redirect:/sensors";
//        }
//
//        movementSensor.setSensorStatus(SensorStatus.NORMAL);
//        movementSensorRepository.save(movementSensor);
//
//        return "redirect:/sensors";
//    }

    public void createMessage(){
        Random random = new Random();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                List<MovementSensor> movementSensors = movementSensorRepository.findAllByPostIsNotNull();
                if (movementSensors.size() != 0) {
                    //random.nextInt(max - min) + min;
                    int index = random.nextInt(movementSensors.size());
                    sensorService.createError(movementSensors.get(index));
                }
                Thread.sleep(1000);
            }
        }).start();
    }
}
