package b22.metro2033.Controller.Delivery;

import b22.metro2033.Entity.Delivery.Courier;
import b22.metro2033.Entity.Delivery.Item;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.UserRepository;
import b22.metro2033.Service.PaginatedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if (user == null) {
            return "redirect:/auth/login";
        }

        List<Item> items = itemRepository.findAll();
        Page<Item> itemsPage = PaginatedService.findPaginated(PageRequest.of(currentPage - 1, pageSize), items);

        model.addAttribute("itemsPage", itemsPage);
        model.addAttribute("start_page", startPage);
        model.addAttribute("number_of_pages", numberOfPages);
        model.addAttribute("current_page", currentPage);

        int totalPages = itemsPage.getTotalPages();

        if (totalPages > 0) {

            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = startPage; i < startPage + numberOfPages; i++){
                if (i > totalPages) break;
                pageNumbers.add(i);
            }
            model.addAttribute("pageNumbers", pageNumbers);
        }

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
