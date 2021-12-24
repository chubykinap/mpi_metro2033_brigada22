package b22.metro2033.functional;

import b22.metro2033.Controller.Army.ArmyController;
import b22.metro2033.Entity.Army.HealthState;
import b22.metro2033.Entity.Army.Rank;
import b22.metro2033.Entity.Delivery.*;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Entity.Utility.OrderItemUtility;
import b22.metro2033.Repository.Army.*;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.Delivery.OrderRepository;
import b22.metro2033.Repository.UserRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static b22.metro2033.Entity.Army.HealthState.CRITICAL;
import static b22.metro2033.Entity.Army.Rank.MAJOR;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("head_courier")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-for-courier-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-for-courier-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CourierTest {

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

    public DeliveryOrder createOrderForCourier(Courier courier){
        Item item = new Item();
        item.setName("AK-47");
        item.setQuantity(4);
        itemRepository.save(item);

        DeliveryOrder order = new DeliveryOrder();
        order.setCourier(courier);
        order.setState(DeliveryState.RECEIVED);
        Date date = new Date();
        order.setDate(date);
        order.setStation("Горьковская");
        order.setPointOfDeparture(false);
        orderRepository.save(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setQuantity(2);
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);

        return order;

    }

    public DeliveryOrder createTestOrder(User user){

        Courier courier = new Courier();
        courier.setWorking(false);
        courier.setUser(user);
        courierRepository.save(courier);

        return createOrderForCourier(courier);
    }

    @Test
    public void testOfShowingCouriers() throws Exception {
        User user = new User();
        user.setRole(Role.COURIER);
        user.setName("TestName");
        user.setSurname("TestSurname");
        user.setPatronymic("TestPatronymic");
        user.setLogin("TestLogin");
        user.setPassword("TestPassword");
        userRepository.save(user);

        Courier courier = new Courier();
        courier.setUser(user);

        courierRepository.save(courier);

        mockMvc.perform(get("/courier"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/div/div[2]/table/tbody/tr[1]/td[1]").string("TestName"))
                .andExpect(status().isOk());

    }

    @Test
    public void testOfShowingAllOrders() throws Exception {
        User user = new User();
        user.setRole(Role.COURIER);
        user.setName("TestName");
        user.setSurname("TestSurname");
        user.setPatronymic("TestPatronymic");
        user.setLogin("TestLogin");
        user.setPassword("TestPassword");
        userRepository.save(user);

        createTestOrder(user);

        mockMvc.perform(get("/delivery"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/div[2]/table/tbody/tr/td[2]").string("Горьковская"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "head_courier", password = "ggg", authorities = "delivery:write")
    public void testOfCreationNewOrder() throws Exception{

        User user = new User();
        user.setRole(Role.COURIER);
        user.setName("TestName");
        user.setSurname("TestSurname");
        user.setPatronymic("TestPatronymic");
        user.setLogin("TestLogin");
        user.setPassword("TestPassword");
        userRepository.save(user);

        Courier courier = new Courier();
        courier.setWorking(false);
        courier.setUser(user);
        courierRepository.save(courier);

        Item item1 = new Item();
        item1.setName("AK-47");
        item1.setQuantity(4);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("RPG");
        item2.setQuantity(10);
        itemRepository.save(item2);

        String items_json_array = "[[\"AK-47\", \"2\"],[\"RPG\", \"2\"]]";

        String response = "{" +
                "\"station\": " + "\"Горьковская\"" + ", " +
                "\"direction\": " + false + ", " +
                "\"date\": " + "\"2033-01-01\"" + ", " +
                "\"courier_id\": " + courier.getId() + ", " +
                "\"items\": " + items_json_array + "}";

        mockMvc.perform(post("/delivery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/delivery"));

        List<DeliveryOrder> changed_order = orderRepository.findAllByCourierId(courier.getId());
        Assertions.assertEquals(changed_order.get(0).getState(), DeliveryState.RECEIVED);

    }

    @Test
    @WithMockUser(username = "head_courier", password = "ggg", authorities = "delivery:write")
    public void testOfCreationNewOrderNegativeJSONBody() throws Exception{

        User user = new User();
        user.setRole(Role.COURIER);
        user.setName("TestName");
        user.setSurname("TestSurname");
        user.setPatronymic("TestPatronymic");
        user.setLogin("TestLogin");
        user.setPassword("TestPassword");
        userRepository.save(user);

        Courier courier = new Courier();
        courier.setWorking(false);
        courier.setUser(user);
        courierRepository.save(courier);

        Item item1 = new Item();
        item1.setName("AK-47");
        item1.setQuantity(4);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("RPG");
        item2.setQuantity(10);
        itemRepository.save(item2);

        String items_json_array = "[[\"AK-47\", \"2\"],[\"RPG\", \"2\"]]";

        String response = "{" +
                "\"station\": " + "\"Горьковская\"" + ", " +
                "\"direction\": " + 1234 + ", " +
                "\"date\": " + "\"Ujjhdbfjs\"" + ", " +
                "\"courier_id\": " + courier.getId() + ", " +
                "\"items\": " + items_json_array + "}";

        mockMvc.perform(post("/delivery")
                .contentType(MediaType.APPLICATION_JSON)
                .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is4xxClientError());

//        List<DeliveryOrder> changed_order = orderRepository.findAllByCourierId(courier.getId());
//        Assertions.assertEquals(changed_order.get(0).getState(), DeliveryState.RECEIVED);

    }
    @Test
    @WithMockUser(username = "head_courier", password = "ggg", authorities = "delivery:write")
    public void testOfCreationNewOrderNegativeJSONSyntax() throws Exception{

        User user = new User();
        user.setRole(Role.COURIER);
        user.setName("TestName");
        user.setSurname("TestSurname");
        user.setPatronymic("TestPatronymic");
        user.setLogin("TestLogin");
        user.setPassword("TestPassword");
        userRepository.save(user);

        Courier courier = new Courier();
        courier.setWorking(false);
        courier.setUser(user);
        courierRepository.save(courier);

        Item item1 = new Item();
        item1.setName("AK-47");
        item1.setQuantity(4);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("RPG");
        item2.setQuantity(10);
        itemRepository.save(item2);

        String items_json_array = "[[\"AK-47\", \"2\"],[\"RPG\", \"2\"]]";

        String response = "{" +
                "\"station\": " + "\"Горьковская\"" + ", " +
                "\"direction\": " + 1234 + ", " +
                "\"date\": " + "\"Ujjhdbfjs\"" + ", " +
                "\"courier_id\": " + courier.getId() + ", " +
                "\"items\": " + items_json_array + "";

        mockMvc.perform(post("/delivery")
                .contentType(MediaType.APPLICATION_JSON)
                .content(response))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is4xxClientError());

//        List<DeliveryOrder> changed_order = orderRepository.findAllByCourierId(courier.getId());
//        Assertions.assertEquals(changed_order.get(0).getState(), DeliveryState.RECEIVED);

    }

    @Test
    @WithMockUser(username = "c", password = "ggg", authorities = "delivery:read")
    public void testConfirmationOfOrderByCourier() throws Exception {

        User user = userRepository.findByLogin("c").orElse(null);

        Courier courier = new Courier();
        courier.setWorking(false);
        courier.setUser(user);
        courierRepository.save(courier);

        Item item = new Item();
        item.setName("AK-47");
        item.setQuantity(4);
        itemRepository.save(item);

        DeliveryOrder order = new DeliveryOrder();
        order.setCourier(courier);
        order.setState(DeliveryState.RECEIVED);
        Date date = new Date();
        order.setDate(date);
        order.setStation("Горьковская");
        order.setPointOfDeparture(true);
        orderRepository.save(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setQuantity(2);
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);

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
        Assertions.assertEquals(changed_order.getState(), DeliveryState.COMPLETED);
    }








}
