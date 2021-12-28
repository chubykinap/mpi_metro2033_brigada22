package b22.metro2033.business_cycle;

import b22.metro2033.Controller.Army.ArmyController;
import b22.metro2033.Entity.Delivery.*;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Army.*;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.Delivery.OrderRepository;
import b22.metro2033.Repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestPropertySource("/application-test.properties")
//@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql"})*/

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateAndCompleteOrder3 {
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

    public Item createTestItem(String name, int quantity){
        Item item = new Item();
        item.setName(name);
        item.setQuantity(quantity);
        itemRepository.save(item);

        return item;
    }

    public OrderItem createTestOrderItem(Item item, DeliveryOrder order){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setQuantity(2);
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);

        return orderItem;
    }

    public DeliveryOrder createOrderForCourier(Courier courier){

        Item item = createTestItem("AK-47", 100);

        DeliveryOrder order = new DeliveryOrder();
        order.setCourier(courier);
        order.setState(DeliveryState.RECEIVED);
        Date date = new Date();
        order.setDate(date);
        order.setStation("Горьковская");
        order.setPointOfDeparture(false);
        orderRepository.save(order);

        createTestOrderItem(item, order);

        return order;

    }

    public Courier createTestCourier(User user){
        Courier courier = new Courier();
        courier.setWorking(false);
        courier.setUser(user);
        courierRepository.save(courier);
        return courier;
    }

    public User createTestUser(){
        User user = new User();
        user.setRole(Role.COURIER);
        user.setName("TestName");
        user.setSurname("TestSurname");
        user.setPatronymic("TestPatronymic");
        user.setLogin("TestLogin");
        user.setPassword("TestPassword");
        userRepository.save(user);
        return user;
    }

    /*@BeforeAll
     void setUpFixture() {

        /*User head_courier = new User();
        head_courier.setRole(Role.HEAD_COURIER);
        head_courier.setName("head_courier1_name");
        head_courier.setSurname("head_courier1_surname");
        head_courier.setPatronymic("head_courier1_patronymic");
        head_courier.setLogin("head_courier1");
        head_courier.setPassword("$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC");
        userRepository.save(head_courier);

        User courier = new User();
        courier.setRole(Role.HEAD_COURIER);
        courier.setName("courier1_name");
        courier.setSurname("courier1_surname");
        courier.setPatronymic("courier1_patronymic");
        courier.setLogin("courier1");
        courier.setPassword("$2a$12$FEV9T2U5Fz/cDPJdSFNPBuz/SdUq0U7AwQnoG6ejHRthisSBXXDtC");
        userRepository.save(courier);
    }*/

    //Курьер открывает список заказов
    @Test
    @Order(1)
    @WithMockUser(username = "head_courier", password = "ggg", authorities = "delivery:read")
    public void testOpenOrdersByHeadCourier() throws Exception{

        mockMvc.perform(get("/delivery"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

    }

    @Test
    @Order(2)
    @WithMockUser(username = "head_courier", password = "ggg", authorities = "delivery:write")
    public void testOpenFormCreateOrderByHeadCourier() throws Exception{

        mockMvc.perform(get("/delivery/create/receive"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "head_courier", password = "ggg", authorities = "delivery:write")
    public void testPostCreateOrderByHeadCourier() throws Exception{

        User user = userRepository.findByLogin("c").orElse(null);

        Courier current_courier = createTestCourier(user);

        Item item1 = createTestItem("AK-47", 100);

        Item item2 = createTestItem("RPG", 200);

        String items_json_array = "[[\"" + item1.getName() + "\", \"" + item1.getQuantity()+ "\"],[\""
                + item2.getName() + "\", \"" + item2.getQuantity()+ "\"]]";

        String response = "{" +
                "\"station\": " + "\"Горьковская\"" + ", " +
                "\"direction\": " + false + ", " +
                "\"date\": " + "\"2033-01-01\"" + ", " +
                "\"courier_id\": " + current_courier.getId() + ", " +
                "\"items\": " + items_json_array + "}";

        mockMvc.perform(post("/delivery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/delivery"));

        List<DeliveryOrder> changed_order = orderRepository.findAllByCourierId(current_courier.getId());
        Assertions.assertEquals(changed_order.get(0).getState(), DeliveryState.RECEIVED);
    }

    @Test
    @Order(4)
    @WithMockUser(username = "c", password = "ggg", authorities = "delivery:read")
    public void testAlertByCourier() throws Exception{
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/div[2]/div/div[1]/div/div/div/strong").string("Info!"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithMockUser(username = "c", password = "ggg", authorities = "delivery:read")
    public void testShowingOrdersByCourier() throws Exception{
        mockMvc.perform(get("/delivery"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    @WithMockUser(username = "c", password = "ggg", authorities = "delivery:read")
    public void testOfShowingOrderByCourier() throws Exception{
        User user = userRepository.findByLogin("c").orElse(null);

        //Assertions.assertEquals("c", user.getLogin());

        DeliveryOrder order = orderRepository.findAll().get(0);

        mockMvc.perform(get("/delivery/view/" + order.getId()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"text_id\"]").string(Long.toString(order.getId())))
                .andExpect(xpath("/html/body/div/div/div[2]/form/div/div[3]/select/option").string("RECEIVED"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    @WithMockUser(username = "c", password = "ggg", authorities = "delivery:read")
    public void testOfChangeStateByCourier() throws Exception{
        User user = userRepository.findByLogin("c").orElse(null);

        Courier current_courier = courierRepository.findByUserId(user.getId()).orElse(null);
        if (current_courier == null){
            current_courier = createTestCourier(user);
        }

        List<DeliveryOrder> deliveryOrder = orderRepository.findAllByCourierId(current_courier.getId());
        DeliveryOrder order = deliveryOrder.get(0);

        String response = "{" +
                "\"id\": " + order.getId() + ", " +
                "\"state\": " + DeliveryState.IN_PROGRESS +
                "}";

        mockMvc.perform(post("/delivery/changeState")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/delivery"));


        DeliveryOrder changed_order = orderRepository.findById(order.getId()).orElse(null);
        Assertions.assertEquals(changed_order.getState(), DeliveryState.IN_PROGRESS);
    }

    @Test
    @Order(8)
    @WithMockUser(username = "c", password = "ggg", authorities = "delivery:read")
    public void testOfEndOrderByCourier() throws Exception{
        User user = userRepository.findByLogin("c").orElse(null);

        Courier current_courier = courierRepository.findByUserId(user.getId()).orElse(null);
        if (current_courier == null){
            current_courier = createTestCourier(user);
        }

        List<DeliveryOrder> deliveryOrder = orderRepository.findAllByCourierId(current_courier.getId());
        DeliveryOrder order = deliveryOrder.get(0);

        String response = "{" +
                "\"id\": " + order.getId() + ", " +
                "\"state\": " + DeliveryState.COMPLETED +
                "}";

        mockMvc.perform(post("/delivery/changeState")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/delivery"));


        DeliveryOrder changed_order = orderRepository.findById(order.getId()).orElse(null);
        Assertions.assertEquals(DeliveryState.CLOSED, changed_order.getState());
    }

    @Test
    @Order(9)
    @WithMockUser(username = "head_courier", password = "ggg", authorities = "delivery:read")
    public void testOpenOrdersByHeadCourierAfterClosedOrder() throws Exception{

        mockMvc.perform(get("/delivery"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    @Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "head_courier", password = "ggg", authorities = "delivery:read")
    public void testShowingOrderClosedByHeadCourier() throws Exception{
        User user = userRepository.findByLogin("courier1").orElse(null);

        Courier current_courier = courierRepository.findByUserId(user.getId()).orElse(null);
        if (current_courier == null){
            current_courier = createTestCourier(user);
        }

        List<DeliveryOrder> deliveryOrder = orderRepository.findAllByCourierId(current_courier.getId());
        DeliveryOrder order = deliveryOrder.get(0);

        mockMvc.perform(get("/delivery/view/" + order.getId()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"text_id\"]").string(Long.toString(order.getId())))
                .andExpect(xpath("/html/body/div/div/div[2]/form/div/div[3]/select/option").string("CLOSED"))
                .andExpect(status().isOk());
    }

}
