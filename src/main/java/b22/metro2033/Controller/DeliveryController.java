package b22.metro2033.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/js/delivery")
public class DeliveryController {

    @GetMapping
    public String index(){
        return "js/delivery/index";
    }

}