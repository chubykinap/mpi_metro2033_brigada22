package b22.metro2033.Controller.Delivery;

import b22.metro2033.Entity.Army.HealthState;
import b22.metro2033.Entity.Army.Post;
import b22.metro2033.Entity.Army.Rank;
import b22.metro2033.Entity.Army.Soldier;
import b22.metro2033.Entity.Delivery.Item;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.UserRepository;
import org.json.JSONObject;
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
@RequestMapping("/storage")
public class StorageController {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public StorageController(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('delivery:read')")
    public String index(Model model, Authentication authentication) {
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }

        List<Item> items = itemRepository.findAll();

        model.addAttribute("items", items);
        return "storage/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('delivery:write')")
    public String create(Model model, Authentication authentication) {
        model.addAttribute("item", new Item());
        model.addAttribute("action", "Create");

        return "storage/form";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('delivery:write')")
    public String create(@ModelAttribute("item") @Valid Item item, BindingResult bindingResult,
                         Model model, Authentication authentication, @RequestParam("action") String action){
        if(bindingResult.hasErrors()) {
            model.addAttribute("action", action);
            model.addAttribute("items", itemRepository.findAll());
            return "storage/form";
        }

        itemRepository.save(item);

        return "redirect:/storage";
    }

    @GetMapping("/change/{id}")
    @PreAuthorize("hasAuthority('delivery:write')")
    public String changeForm(Model model, Authentication authentication, @PathVariable Long id){

        Item item = itemRepository.findById(id).orElse(null);
        if(item == null){
            return "redirect:/storage";
        }

        model.addAttribute("item", item);
        model.addAttribute("action", "change");

        return "storage/change";
    }

    @PreAuthorize("hasAuthority('delivery:write')")
    @RequestMapping(value = "/change", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String change(@RequestBody String response) throws Exception { //ParesException type?

        JSONObject json = new JSONObject(response);

        long item_id = Long.parseLong(json.getString("item_id"));
        Item item = itemRepository.findById(item_id).orElse(null);
        if(item == null) {
            return "redirect:/storage";
        }

        item.setName(json.getString("name"));
        item.setQuantity(json.getInt("quantity"));
        itemRepository.save(item);

        return "redirect:/storage";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String delete(@PathVariable Long id) {

        Item item = itemRepository.findById(id).orElse(null);

        if(item != null){
            itemRepository.deleteById(id);
        }

        return "redirect:/storage";
    }
}
