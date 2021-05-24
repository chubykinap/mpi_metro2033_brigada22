package b22.metro2033.Controller.Engineering;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/request")
public class RequestController {

    @GetMapping
    public String index(){
        return "request/index";
    }

}