package b22.metro2033.business_cycle;

import b22.metro2033.Controller.Army.ArmyController;
import b22.metro2033.Entity.Alerts.AlertMessages;
import b22.metro2033.Entity.Alerts.TypeOfMessage;
import b22.metro2033.Entity.Delivery.*;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Alerts.AlertsRepository;
import b22.metro2033.Repository.Army.*;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.Delivery.OrderRepository;
import b22.metro2033.Repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
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

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("head_courier")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateAndCompleteOrder4 {

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

    public DeliveryOrder createOrderForCourier(Courier courier, DeliveryState deliveryState){

        Item item = createTestItem("AK-47", 100);

        DeliveryOrder order = new DeliveryOrder();
        order.setCourier(courier);
        order.setState(deliveryState);
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
        courier.setOrder(null);
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

    public void sendAlertMessage(User user, String message, TypeOfMessage type) {
        AlertMessages alertMessages = new AlertMessages();
        alertMessages.setUser(user);
        alertMessages.setAlert_message(message);
        alertMessages.setType_of_message(type);

        alertsRepository.save(alertMessages);
    }

    //Главынй курьер просматривает страницу с заказами
    @Test
    @Order(1)
    @WithMockUser(username = "head_courier", password = "ggg", authorities = "delivery:read")
    public void testOpenOrdersByHeadCourier() throws Exception{

        mockMvc.perform(get("/delivery"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

    }

    //Главынй курьер переходит на страницу создания заказа
    @Test
    @Order(2)
    @WithMockUser(username = "head_courier", password = "ggg", authorities = "delivery:write")
    public void testOpenFormCreateOrderByHeadCourier() throws Exception{

        mockMvc.perform(get("/delivery/create/receive"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    //Главынй курьер создает заказ
    @Test
    @Order(3)
    @WithMockUser(username = "head_courier", password = "ggg", authorities = "delivery:write")
    public void testPostCreateOrderByHeadCourier() throws Exception{

        User user = userRepository.findByLogin("c").orElse(null);
        Courier courier = createTestCourier(user);

        Item item1 = createTestItem("AK-47", 100);
        Item item2 = createTestItem("RPG", 200);

        String items_json_array = "[[\"" + item1.getName() + "\", \"" + item1.getQuantity()+ "\"],[\""
                + item2.getName() + "\", \"" + item2.getQuantity()+ "\"]]";

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

    //Курьеру приходит уведомление о новом заказе
    @Test
    @Order(4)
    @WithMockUser(username = "c", password = "ggg", authorities = "delivery:read")
    public void testAlertByCourier() throws Exception{

        User user = userRepository.findByLogin("c").orElse(null);
        Courier courier = createTestCourier(user);
        DeliveryOrder order = createOrderForCourier(courier, DeliveryState.RECEIVED);
        sendAlertMessage(user, "Вам назначен заказ", TypeOfMessage.NOTIFICATION);

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div/div[2]/div/div[1]/div/div/div/strong").string("Info!"))
                .andExpect(status().isOk());
    }

    //Курьеру переходит на страницу заказа
    @Test
    @Order(5)
    @WithMockUser(username = "c", password = "ggg", authorities = "delivery:read")
    public void testShowingOrdersByCourier() throws Exception{

        User user = userRepository.findByLogin("c").orElse(null);
        Courier courier = createTestCourier(user);
        DeliveryOrder order = createOrderForCourier(courier, DeliveryState.RECEIVED);

        mockMvc.perform(get("/delivery"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    //Курьеру просматривает инфомрацию о заказе
    @Test
    @Order(6)
    @WithMockUser(username = "c", password = "ggg", authorities = "delivery:read")
    public void testOfShowingOrderByCourier() throws Exception{
        User user = userRepository.findByLogin("c").orElse(null);
        Courier courier = createTestCourier(user);
        DeliveryOrder order = createOrderForCourier(courier, DeliveryState.RECEIVED);

        mockMvc.perform(get("/delivery/view/" + order.getId()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"text_id\"]").string(Long.toString(order.getId())))
                .andExpect(xpath("//*[@id=\"state\"]/option[1]").string("RECEIVED"))
                .andExpect(status().isOk());
    }

    //Курьеру просматривает инфомрацию о заказе
    @Test
    @Order(7)
    @WithMockUser(username = "c", password = "ggg", authorities = "delivery:read")
    public void testOfChangeStateByCourier() throws Exception{
        User user = userRepository.findByLogin("c").orElse(null);
        Courier courier = createTestCourier(user);
        DeliveryOrder order = createOrderForCourier(courier, DeliveryState.RECEIVED);

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

    //Курьер завершает заказ
    @Test
    @Order(8)
    @WithMockUser(username = "c", password = "ggg", authorities = "delivery:read")
    public void testOfEndOrderByCourier() throws Exception{
        User user = userRepository.findByLogin("c").orElse(null);
        Courier courier = createTestCourier(user);
        DeliveryOrder order = createOrderForCourier(courier, DeliveryState.IN_PROGRESS);

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

    //Главный курьер заходит на страницу заказов
    @Test
    @Order(9)
    @WithMockUser(username = "head_courier", password = "ggg", authorities = "delivery:read")
    public void testOpenOrdersByHeadCourierAfterClosedOrder() throws Exception{

        testOfEndOrderByCourier();

        mockMvc.perform(get("/delivery"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    //Главный курьер смотрит что заказ завершен
    @Test
    @Order(10)
    //@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "head_courier", password = "ggg", authorities = "delivery:read")
    public void testShowingOrderClosedByHeadCourier() throws Exception{
        User user = userRepository.findByLogin("c").orElse(null);
        Courier courier = createTestCourier(user);
        DeliveryOrder order = createOrderForCourier(courier, DeliveryState.CLOSED);

        mockMvc.perform(get("/delivery/view/" + order.getId()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"text_id\"]").string(Long.toString(order.getId())))
                .andExpect(xpath("//*[@id=\"state\"]/option[1]").string("CLOSED"))
                .andExpect(status().isOk());
    }

}
