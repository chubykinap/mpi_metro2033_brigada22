package b22.metro2033.Controller.Engineering;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/engineering")
public class EngineerController {

    @GetMapping
    public String index(){
        return "engineering/index";
    }

}
