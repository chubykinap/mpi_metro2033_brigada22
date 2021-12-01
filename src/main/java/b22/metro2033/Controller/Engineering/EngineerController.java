package b22.metro2033.Controller.Engineering;

import b22.metro2033.Entity.Engineering.Engineer;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.EngineerUtility;
import b22.metro2033.Entity.Utility.SoldierUtility;
import b22.metro2033.Repository.Engineering.EngineerRepository;
import b22.metro2033.Repository.Engineering.RequestRepository;
import b22.metro2033.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/engineers")
public class EngineerController {
    private final EngineerRepository engineerRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Autowired
    public EngineerController(EngineerRepository engineerRepository, RequestRepository requestRepository, UserRepository userRepository) {
        this.engineerRepository = engineerRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('engineers:read')")
    public String index(Model model, Authentication authentication){
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        if(user == null){
            return "redirect:/auth/login";
        }
        List<Engineer> engineers = engineerRepository.findAll();
        model.addAttribute("engineers", EngineerUtility.toEngineerUtility(engineers));

        return "engineers/index";
    }


    @GetMapping
    public String index(){
        return "engineers/index";
    }

}
