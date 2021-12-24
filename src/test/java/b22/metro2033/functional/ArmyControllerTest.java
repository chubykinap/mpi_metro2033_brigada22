package b22.metro2033.functional;

import b22.metro2033.Controller.Army.ArmyController;
import b22.metro2033.Entity.Army.*;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Army.*;
import b22.metro2033.Repository.UserRepository;
import org.checkerframework.checker.units.qual.A;
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

import java.time.LocalDateTime;
import java.util.List;

import static b22.metro2033.Entity.Army.HealthState.CRITICAL;
import static b22.metro2033.Entity.Army.Rank.MAJOR;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Autowired
    private MovementSensorRepository movementSensorRepository;

    @Autowired
    private SensorMessagesRepository sensorMessagesRepository;

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
    void testCreateNewSoldierNegativeJSONSyntax() throws Exception {
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
                "\"rank\": 1, " +
                "\"health_state\":\"Здоров\", " +
                "\"user_id\": " + user.getId() + ", " +
                "\"post_id\": " + post.getId() + "," +
                "\"agility\": \"1\", ";

        mockMvc.perform(post("/army")
                .contentType(MediaType.APPLICATION_JSON)
                .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCreateNewSoldierNegativeJSONBody() throws Exception {
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
                "\"rank\": 1, " +
                "\"health_state\":\"Здоров\", " +
                "\"user_id\": " + user.getId() + ", " +
                "\"post_id\": " + post.getId() + "," +
                "\"agility\": \"1\", " +
                "\"strength\": 2, " +
                "\"stamina\": 3" +
                "}";

        mockMvc.perform(post("/army")
            .contentType(MediaType.APPLICATION_JSON)
            .content(response))
            .andDo(print())
            .andExpect(authenticated())
            .andExpect(status().is4xxClientError());
    }

    @Test
    void testChangeOneSoldier() throws Exception {
        testCreateNewSoldier();
        List<Soldier> soldierList = soldierRepository.findAll();
        Soldier soldier = soldierList.get(0);
        long id = soldier.getId();

        Post new_post = new Post();
        new_post.setLocation("Vagon");
        new_post.setName("post_3");

        postRepository.save(new_post);

        // change all
        String response = "{" +
                "\"soldier_id\": " + soldier.getId() + ", " +
                "\"rank\": \"" + Rank.getStateRU(MAJOR) + "\", " +
                "\"health_state\": \"" + HealthState.getStateRU(CRITICAL) + "\", " +
                "\"user_id\": " + soldier.getUser().getId() + ", " +
                "\"post_id\": " + new_post.getId() + "," +
                "\"agility\": " + 33 + ", " +
                "\"strength\": " + 44 + ", " +
                "\"stamina\": " + 55 +
                "}";

        mockMvc.perform(post("/army/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/army"));

        //Get soldier
        Soldier changedSoldier = soldierRepository.findById(soldier.getId()).orElse(null);

        //Rank
        Rank rank = changedSoldier.getRank();
        Assertions.assertEquals(rank, MAJOR);

        //Health_state
        HealthState healthState = changedSoldier.getHealth_state();
        Assertions.assertEquals(healthState, CRITICAL);

        //Post
        long post_id = changedSoldier.getPost().getId();
        Assertions.assertEquals(post_id, new_post.getId());

        int agility = changedSoldier.getCharacteristics().getAgility();
        Assertions.assertEquals(agility, 33);

        int strength = changedSoldier.getCharacteristics().getStrength();
        Assertions.assertEquals(strength, 44);

        int stamina = changedSoldier.getCharacteristics().getStamina();
        Assertions.assertEquals(stamina, 55);
    }

    @Test
    void testChangeOneSoldierNegativeJSONBody() throws Exception {
        testCreateNewSoldier();
        List<Soldier> soldierList = soldierRepository.findAll();
        Soldier soldier = soldierList.get(0);
        long id = soldier.getId();

        Post new_post = new Post();
        new_post.setLocation("Vagon");
        new_post.setName("post_3");

        postRepository.save(new_post);

        // change all
        String response = "{" +
                "\"soldier_id\": ываыва, " +
                "\"rank\": \"" + Rank.getStateRU(MAJOR) + "\", " +
                "\"health_state\": \"" + HealthState.getStateRU(CRITICAL) + "\", " +
                "\"user_id\": " + soldier.getUser().getId() + ", " +
                "\"post_id\": " + new_post.getId() + "," +
                "\"agility\": " + 33 + ", " +
                "\"strength\": " + 44 + ", " +
                "\"stamina\": " + 55 +
                "}";

        mockMvc.perform(post("/army/change")
                .contentType(MediaType.APPLICATION_JSON)
                .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is4xxClientError());
    }
    @Test
    void testChangeOneSoldierNegativeJSONSyntax() throws Exception {
        testCreateNewSoldier();
        List<Soldier> soldierList = soldierRepository.findAll();
        Soldier soldier = soldierList.get(0);
        long id = soldier.getId();

        Post new_post = new Post();
        new_post.setLocation("Vagon");
        new_post.setName("post_3");

        postRepository.save(new_post);

        // change all
        String response = "{" +
                "\"soldier_id\": ываыва, " +
                "\"rank\": \"" + Rank.getStateRU(MAJOR) + "\", " +
                "\"health_state\": \"" + HealthState.getStateRU(CRITICAL) + "\", " +
                "\"user_id\": " + soldier.getUser().getId() + ", " +
                "\"post_id\": " + new_post.getId() + "," +
                "\"agility\": " + 33 + ", " +
                "\"strength\": " + 44 + ", " +
                "\"stamina\": " + 55 +
                "";

        mockMvc.perform(post("/army/change")
                .contentType(MediaType.APPLICATION_JSON)
                .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is4xxClientError());
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

    @Test
    void testDeleteOneSoldierNegativeID() throws Exception {
        testCreateNewSoldier();
        List<Soldier> soldierList = soldierRepository.findAll();
        Soldier soldier = soldierList.get(soldierList.size() - 1);
        long id = soldier.getId() + 1000;

        mockMvc.perform(get("/army/delete/" + soldier.getId()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/army"));

//        Assertions.assertNull(soldierRepository.findById(soldier.getId()).orElse(null));
    }

    @Test
    void testOfShowingSensors() throws Exception {
        Post post = new Post();
        post.setLocation("Vagon");
        post.setName("post_3");

        postRepository.save(post);

        MovementSensor movementSensor = new MovementSensor();
        movementSensor.setSensorStatus(SensorStatus.NORMAL);
        movementSensor.setLocation("vhod");
        movementSensor.setName("sensor1");
        movementSensor.setPost(post);

        movementSensorRepository.save(movementSensor);

        SensorMessages sensorMessages = new SensorMessages();
        sensorMessages.setMessages("All good");
        sensorMessages.setMessages_date(LocalDateTime.now());
        sensorMessages.setError(false);
        sensorMessages.setMovementSensor(movementSensorRepository.findById(movementSensor.getId()).orElse(null));

        sensorMessagesRepository.save(sensorMessages);

        mockMvc.perform(get("/sensors"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/div[2]/div[2]/table/tbody/tr[1]/div[1]/td[1]").string("sensor1"))
                .andExpect(xpath("/html/body/div/div/div[2]/div[2]/table/tbody/tr[1]/div[1]/td[2]").string("vhod"))
                .andExpect(xpath("/html/body/div/div/div[2]/div[2]/table/tbody/tr[1]/div[1]/div[1]/td[1]").string("Название: post_3Расположение: Vagon"))
                .andExpect(status().isOk());
    }

    @Test
    void testSoldierAppointmentToPost() throws Exception{

        testCreateNewSoldier();
        List<Soldier> soldierList = soldierRepository.findAll();
        Soldier soldier = soldierList.get(0);
        long id = soldier.getId();

        Post post = new Post();
        post.setLocation("Vagon");
        post.setName("post_3");

        postRepository.save(post);

        // change all
        String response = "{" +
                "\"post_id\": " + post.getId() + ", " +
                "\"soldier_id\": \"" + id + "\"" +
                "}";

        mockMvc.perform(post("/posts/add_soldier_to_post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/posts"));

        //Get soldier
        Soldier changedSoldier = soldierRepository.findById(soldier.getId()).orElse(null);

        Assertions.assertEquals(post.getId(), changedSoldier.getPost().getId());

    }

    @Test
    void testSoldierAppointmentToPostNegativeIDs() throws Exception{

        testCreateNewSoldier();
        List<Soldier> soldierList = soldierRepository.findAll();
        Soldier soldier = soldierList.get(0);
        long id = soldier.getId() + 1098923;

        Post post = new Post();
        post.setLocation("Vagon");
        post.setName("post_3");

        postRepository.save(post);

        // change all
        String response = "{" +
                "\"post_id\": " + post.getId() + ", " +
                "\"soldier_id\": \"" + id + "\"" +
                "}";

        mockMvc.perform(post("/posts/add_soldier_to_post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/posts"));

//        //Get soldier
//        Soldier changedSoldier = soldierRepository.findById(soldier.getId()).orElse(null);
//
//        Assertions.assertEquals(post.getId(), changedSoldier.getPost().getId());

    }

}