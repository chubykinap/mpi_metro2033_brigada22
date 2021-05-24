package b22.metro2033.Controller;

import b22.metro2033.Entity.Army.Soldier;
import b22.metro2033.Entity.Delivery.DeliveryOrder;
import b22.metro2033.Repository.Army.CharacteristicsRepository;
import b22.metro2033.Repository.Delivery.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class test {
    private final CharacteristicsRepository repository;
    private final OrderRepository repositoryOrder;

    public test(CharacteristicsRepository repository, OrderRepository repositoryOrder) {
        this.repository = repository;
        this.repositoryOrder = repositoryOrder;
    }


 /*   @GetMapping("/test")
    public void test() {
        Characteristics characteristics = repository.findBySoldier_id(1);
        System.out.println(characteristics.getAgility());
    }*/

    @GetMapping("/add")
    public String load() {
        return "/add";
    }


    @PostMapping("/add")
    public String add(@RequestParam("text") String text) {
        List<DeliveryOrder> orderList =
                repositoryOrder.findAllByCouriersId(Integer.parseInt(text));
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
