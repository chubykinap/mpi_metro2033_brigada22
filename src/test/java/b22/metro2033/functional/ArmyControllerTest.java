package b22.metro2033.functional;

import b22.metro2033.Controller.Army.ArmyController;
import b22.metro2033.Entity.Army.HealthState;
import b22.metro2033.Entity.Army.Post;
import b22.metro2033.Entity.Army.Rank;
import b22.metro2033.Entity.Army.Soldier;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Army.CharacteristicsRepository;
import b22.metro2033.Repository.Army.PostRepository;
import b22.metro2033.Repository.Army.SoldierRepository;
import b22.metro2033.Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("ggg")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-for-army-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-for-army-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ArmyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArmyController armyController;

    @Autowired
    private SoldierRepository soldierRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CharacteristicsRepository characteristicsRepository;

    @Test
    void testOfShowingAllSoldiersIndex() throws Exception {
        List<Soldier> soldierList = soldierRepository.findAll();

        mockMvc.perform(get("/army"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

//        Assertions.assertAll(soldierRepository.findAll().orElse(null));
    }

    //
    @Test
    void testCreateNewSoldier() throws Exception {
        User user = new User();
        user.setRole(Role.SOLDIER);
        user.setName("TestName");
        user.setSurname("TestSurname");
        user.setPatronymic("TestPatronymic");
        user.setLogin("TestLogin");
        user.setPassword("TestPassword");
        userRepository.save(user);

        Post post = new Post();
        post.setName("TestName");
        post.setLocation("TestLocation");
        postRepository.save(post);

        String response = "{" +
                "\"rank\": \"Рядовой\", " +
                "\"health_state\":\"Здоров\", " +
                "\"user_id\": " + user.getId() + ", " +
                "\"post_id\": " + post.getId() + "," +
                "\"agility\": 1, " +
                "\"strength\": 2, " +
                "\"stamina\": 3" +
                "}";

        mockMvc.perform(post("/army")
                .contentType(MediaType.APPLICATION_JSON)
                .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/army"));

        Assertions.assertNotNull(soldierRepository.findByUserId(user.getId()).orElse(null));
    }

    @Test
    void testChangeOneSoldier() throws Exception {
        testCreateNewSoldier();
        List<Soldier> soldierList = soldierRepository.findAll();
        Soldier soldier = soldierList.get(0);
        long id = soldier.getId();

        // change stamina
        String response = "{" +
                "\"soldier_id\": " + soldier.getId() + ", " +
                "\"rank\": \"" + Rank.getStateRU(soldier.getRank()) + "\", " +
                "\"health_state\": \"" + HealthState.getStateRU(soldier.getHealth_state()) + "\", " +
                "\"user_id\": " + soldier.getUser().getId() + ", " +
                "\"post_id\": " + soldier.getPost().getId() + "," +
                "\"agility\": " + soldier.getCharacteristics().getAgility() + ", " +
                "\"strength\": " + soldier.getCharacteristics().getStrength() + ", " +
                "\"stamina\": " + 55 +
                "}";

        mockMvc.perform(post("/army/change")
                .contentType(MediaType.APPLICATION_JSON)
                .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/army"));

        Soldier changedSoldier = soldierRepository.findById(soldier.getId()).orElse(null);
        int stamina = changedSoldier.getCharacteristics().getStamina();
        Assertions.assertEquals(stamina, 55);
    }

    @Test
    void testDeleteOneSoldier() throws Exception {
        testCreateNewSoldier();
        List<Soldier> soldierList = soldierRepository.findAll();
        Soldier soldier = soldierList.get(0);
        long id = soldier.getId();

        mockMvc.perform(get("/army/delete/" + soldier.getId()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/army"));

        Assertions.assertNull(soldierRepository.findById(soldier.getId()).orElse(null));
    }

}