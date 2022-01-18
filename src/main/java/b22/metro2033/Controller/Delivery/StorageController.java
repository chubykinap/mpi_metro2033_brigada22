package b22.metro2033.Controller.Delivery;

import b22.metro2033.Entity.Delivery.Item;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/storage")
public class StorageController {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public StorageController(ItemRepository itemRepository,
                             UserRepository userRepository,
                             OrderItemRepository orderItemRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
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

    @GetMapping("/change/{id}")
    @PreAuthorize("hasAuthority('delivery:write')")
    public String changeForm(Model model, Authentication authentication, @PathVariable Long id) {

        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            return "redirect:/storage";
        }

        model.addAttribute("item", item);
        model.addAttribute("action", "change");

        return "storage/change";
    }
}
