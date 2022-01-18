package b22.metro2033.Controller.Delivery;

import b22.metro2033.Entity.Delivery.Courier;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.SoldierUtility;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.UserRepository;
import b22.metro2033.Service.PaginatedService;
import b22.metro2033.domain.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/courier")
public class CourierController {
    private final UserRepository userRepository;
    private final CourierRepository courierRepository;

    @Autowired
    public CourierController(UserRepository userRepository,
                             CourierRepository courierRepository) {
        this.userRepository = userRepository;
        this.courierRepository = courierRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('delivery:write')")
    public String index(Model model, Authentication authentication,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size,
                        @RequestParam("start_page") Optional<Integer> start_page,
                        @RequestParam("number_of_pages") Optional<Integer> number_of_pages) {

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

        List<Courier> couriers = courierRepository.findAll();
        Page<Courier> courierPage = PaginatedService.findPaginated(PageRequest.of(currentPage - 1, pageSize), couriers);

        model.addAttribute("courierPage", courierPage);
        model.addAttribute("start_page", startPage);
        model.addAttribute("number_of_pages", numberOfPages);
        model.addAttribute("current_page", currentPage);

        int totalPages = courierPage.getTotalPages();

        if (totalPages > 0) {

            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = startPage; i < startPage + numberOfPages; i++){
                if (i > totalPages) break;
                pageNumbers.add(i);
            }
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "courier/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('delivery:write')")
    public String createForm(Model model, Authentication authentication){
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if(user == null)
            return "redirect:/auth/login";

        model.addAttribute("courier", new Courier());
        model.addAttribute("action", "Create");
        List<User> users = userRepository.findFreeCourier();
        if (users.size() == 0){
            model.addAttribute("users", "NoData");
            return "courier/form";
        }
        model.addAttribute("users", users);

        return "courier/form";
    }

//    @PostMapping("/create")
//    @PreAuthorize("hasAuthority('delivery:write')")
//    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
//    public Response create(@RequestBody String response) throws Exception {
//        JSONObject json = new JSONObject(response);
//
//        long user_id = Long.parseLong(json.getString("user_id"));
//        Courier courier = new Courier();
//        User user = userRepository.findById(user_id).orElse(null);
//
//        if (user == null){
//            return new Response("Error", "Пользователь не найден!");
//        }
//
//        if (user.getCourier() != null || user.getSoldier() != null){
//            return new Response("Error", "Пользователь уже назначен!");
//        }
//
//        courier.setUser(user);
//        courierRepository.save(courier);
//
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("login", user.getLogin());
//        hashMap.put("name", user.getName());
//        hashMap.put("surname", user.getSurname());
//
//        return new Response("Done", hashMap);
//    }

//    @GetMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('delivery:write')")
//    public String remove(@PathVariable Long id) {
//
//        Courier courier = courierRepository.findById(id).orElse(null);
//
//        if(courier != null)
//            courierRepository.deleteById(id);
//
//        return "redirect:/courier";
//    }

    private List<Role> getRolesForSelect(Authentication authentication){
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);

        List<Role> roles = new ArrayList<>();

        if(user == null){
            return roles;
        }

        switch (user.getRole()){
            case HEAD_COURIER:
                roles = Stream.of(Role.COURIER).collect(Collectors.toList());
                break;
            case ADMIN:
                roles = Stream.of(Role.HEAD_COURIER, Role.COURIER).collect(Collectors.toList());
                break;
        }

        return roles;
    }
}

