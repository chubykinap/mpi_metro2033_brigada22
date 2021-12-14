package b22.metro2033.Controller.Army;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("ggg")
class ArmyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArmyController armyController;

    @Test
    void index() throws Exception {
        mockMvc.perform(get("/army"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    void createForm() {
    }

    @Test
    void create() throws Exception {
//        List<Soldier> soldiers = soldierRepository.findAll();
//        List<Soldier> soldiersTest = soldiers;
//
//        JSONObject response = new JSONObject(
//                "{" +
//                        "\"rank\": \"Рядовой\", " +
//                        "\"health_state\":\"Здоров\", " +
//                        "\"user_id\": 100, " +
//                        "\"post_id\": 1," +
//                        "\"agility\": 12, " +
//                        "\"strength\": 12, " +
//                        "\"stamina\": 12" +
//                        "}");
//
//        Characteristics c1 = new Characteristics(12,12,12);
//        Rank rank = Rank.findState("Рядовой");
//        HealthState healthState = HealthState.findState("Здоров");
//        long user_id = 1;
//        long post_id = 1;
//        User user = userRepository.findById(user_id).orElse(null);
//        Post post = postRepository.findById(post_id).orElse(null);
//
//
//        Soldier soldier = new Soldier();
    }

    @Test
    void changeForm() {
    }

    @Test
    void change() {
    }

    @Test
    void enable() {
    }
}