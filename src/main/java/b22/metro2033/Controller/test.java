package b22.metro2033.Controller;

import b22.metro2033.Entity.Army.Characteristics;
import b22.metro2033.Entity.Army.Soldier;
import b22.metro2033.Repository.CharacteristicsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class test {
    private final CharacteristicsRepository repository;

    public test(CharacteristicsRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/test")
    public void test() {
        Characteristics characteristics = repository.findBySoldier_id(1);
        System.out.println(characteristics.getAgility());
    }

    @GetMapping("/add")
    public String load() {
        return "/add";
    }


    @PostMapping("/add")
    public String add(@RequestParam("text") String text) {
        int i = Integer.parseInt(text);
        return "/add";
    }

    @GetMapping("/object_add_template")
    public String loadObject(@ModelAttribute("soldier") Soldier soldier) {

        return "/object";
    }

    @PostMapping("/object_add_template")
    public String addObject(@ModelAttribute("soldier") Soldier soldier) {

        return "/object";
    }
}
