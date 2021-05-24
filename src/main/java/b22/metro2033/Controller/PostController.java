package b22.metro2033.Controller;

import b22.metro2033.Entity.Army.Post;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Army.PostRepository;
import b22.metro2033.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    @Autowired
    public PostController(PostRepository postRepository,
                          UserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('army:write')")
    public String delete(@PathVariable Long id) {

        Post post = postRepository.findById(id).orElse(null);

        if(post != null){
            postRepository.deleteById(id);
        }

        return "redirect:/posts";
    }


}
