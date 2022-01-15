package b22.metro2033.business_cycle;

import b22.metro2033.Controller.Army.ArmyController;
import b22.metro2033.Entity.Army.*;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Alerts.AlertsRepository;
import b22.metro2033.Repository.Army.*;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.Delivery.OrderRepository;
import b22.metro2033.Repository.UserRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//import org.junit.jupiter.api.Order;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class ArmyTest {

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

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AlertsRepository alertsRepository;

    //Генерал просматривает список солдат
    @Test
    @WithMockUser(username = "ggg", password = "ggg", authorities = "army:read")
    public void test1_OpenSoldiersByGeneral() throws Exception{

        mockMvc.perform(get("/army"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    //Генерал открывает страницу с добавлением солдата
    @Test
    @WithMockUser(username = "ggg", password = "ggg", authorities = "army:write")
    public void test2_AddPageByGeneral() throws Exception{

        mockMvc.perform(get("/army/create"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    //Генерал создает нового солдата
    @Test
    @WithMockUser(username = "ggg", password = "ggg", authorities = "army:write")
    public void test3_CreateNewSoldierByGeneral() throws Exception {
        User user = userRepository.findByLogin("s1").orElse(null);
        Post post = createTestPost();

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

    //Курьер смотрит на какой пост его назначили
    @Test
    @WithMockUser(username = "s1", password = "ggg", authorities = "army:read")
    public void test4_CheckNewSoldierByGeneral() throws Exception {
        User user = userRepository.findByLogin("s1").orElse(null);
        Post post = createTestPost();
        createNewSoldier(user, post);

        mockMvc.perform(get("/about_me"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/div[2]/div/div/h6[1]").string("Название - PostTestName"))
                .andExpect(status().isOk());
    }

    //Курьер видит прдупреждения о замеченом движении от датчика
    @Test
    @WithMockUser(username = "s1", password = "ggg", authorities = "army:read")
    public void test5_SensorsAlertsBySoldier() throws Exception {
        User user = userRepository.findByLogin("s1").orElse(null);
        Post post = createTestPost();
        createNewSoldier(user, post);
        MovementSensor movementSensor = createSensorError(post);

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"sensors_alert_messages\"]/div/div/strong").string("Danger!"))
                .andExpect(status().isOk());
    }

    //Генерал просматривает сенсор с предупреждением
    @Test
    @WithMockUser(username = "ggg", password = "ggg", authorities = "army:read")
    public void test6_CheckSensorInfoByGeneral() throws Exception{
        Post post = createTestPost();
        MovementSensor movementSensor = createSensorError(post);

        mockMvc.perform(get("/sensors/messages/" + movementSensor.getId()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/div[2]/table/tbody/tr/div/td[3]").string("ERROR"))
                .andExpect(status().isOk());
    }

    //Генерал подтверждает что проблема решена
    @Test
    @WithMockUser(username = "ggg", password = "ggg", authorities = "army:write")
    public void test7_ProblemDoneByGeneral() throws Exception{
        Post post = createTestPost();
        MovementSensor movementSensor = createSensorError(post);

        mockMvc.perform(get("/sensors/done/" + movementSensor.getId()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/sensors"));
    }

    public User createTestUser(){
        User user = new User();
        user.setRole(Role.SOLDIER);
        user.setName("TestName");
        user.setSurname("TestSurname");
        user.setPatronymic("TestPatronymic");
        user.setLogin("TestLogin");
        user.setPassword("TestPassword");
        userRepository.save(user);
        return user;
    }

    public Post createTestPost(){
        Post post = new Post();
        post.setName("PostTestName");
        post.setLocation("PostTestLocation");
        postRepository.save(post);
        return post;
    }

    public Soldier createNewSoldier(User user, Post post){
        Soldier soldier = new Soldier();
        soldier.setRank(Rank.CADET);
        soldier.setUser(user);
        soldier.setPost(post);
        soldier.setHealth_state(HealthState.HEALTHY);
        soldierRepository.save(soldier);

        Characteristics characteristics = new Characteristics();
        characteristics.setStrength(100);
        characteristics.setStamina(100);
        characteristics.setAgility(100);
        characteristics.setSoldier(soldier);
        characteristicsRepository.save(characteristics);

        return soldier;
    }

    public MovementSensor createSensorError(Post post){
        MovementSensor movementSensor = new MovementSensor();
        movementSensor.setSensorStatus(SensorStatus.ERROR);
        movementSensor.setName("Sensor1");
        movementSensor.setLocation("Vagon");
        movementSensor.setPost(post);

        movementSensorRepository.save(movementSensor);

        SensorMessages sensorMessages = new SensorMessages();
        sensorMessages.setMessages("Возникла ошибка");
        sensorMessages.setMessages_date(LocalDateTime.now());
        sensorMessages.setError(true);
        sensorMessages.setMovementSensor(movementSensor);

        sensorMessagesRepository.save(sensorMessages);

        return movementSensor;
    }

}
