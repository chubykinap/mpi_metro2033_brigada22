package b22.metro2033.volume;

import b22.metro2033.Entity.Delivery.*;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.Army.CharacteristicsRepository;
import b22.metro2033.Repository.Army.MovementSensorRepository;
import b22.metro2033.Repository.Army.PostRepository;
import b22.metro2033.Repository.Army.SensorMessagesRepository;
import b22.metro2033.Repository.Delivery.CourierRepository;
import b22.metro2033.Repository.Delivery.ItemRepository;
import b22.metro2033.Repository.Delivery.OrderItemRepository;
import b22.metro2033.Repository.Delivery.OrderRepository;
import b22.metro2033.Repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VolumeTest {
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

    @BeforeEach
    @AfterEach
    void tearDown() {
        orderItemRepository.deleteAll();
        itemRepository.deleteAll();
        courierRepository.deleteAll();
        orderRepository.deleteAll();
        sensorMessagesRepository.deleteAll();
        movementSensorRepository.deleteAll();
        postRepository.deleteAll();
        characteristicsRepository.deleteAll();
        userRepository.deleteAll();
    }

    public static int rnd(int max)
    {
        return (int) (Math.random() * ++max);
    }

    void createOrderPipeline(int actual) {

        //Create users
        List<User> users = new ArrayList<>();
        for (int i = 0; i < actual; i++) {
            User user = new User();
            user.setRole(Role.ADMIN);
            user.setName("TestName" + i);
            user.setSurname("TestSurname" +  i);
            user.setPatronymic("TestPatronymic" + i);
            user.setLogin("TestLogin" + i);
            user.setPassword("TestPassword" + i);
            users.add(user);
        }
        userRepository.saveAll(users);

        //Create couriers
        List<Courier> couriers = new ArrayList<>();
        for (int i = 0; i < actual; i++) {
            Courier courier = new Courier();
            courier.setWorking(false);
            courier.setUser(users.get(i));
            couriers.add(courier);
        }
        courierRepository.saveAll(couriers);

        //Create items
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < actual; i++) {
            Item item = new Item();
            item.setName("item_test_name" + i);
            item.setQuantity(200);
            items.add(item);
        }
        itemRepository.saveAll(items);

        //Create orders
        List<DeliveryOrder> orders = new ArrayList<>();
        for (int i = 0; i < actual; i++) {
            DeliveryOrder order = new DeliveryOrder();
            order.setCourier(couriers.get(i));
            order.setState(DeliveryState.RECEIVED);
            Date date = new Date();
            order.setDate(date);
            order.setStation("Горьковская" + i);
            order.setPointOfDeparture(false);
            orders.add(order);
        }
        orderRepository.saveAll(orders);

        //Add items
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < actual; i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(items.get(i));
            orderItem.setQuantity(2);
            orderItem.setOrder(orders.get(i));
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);

    }

    void createOnlyOrders(int actual) {
        //Create users
        List<User> users = new ArrayList<>();
        for (int i = 0; i < actual; i++) {
            User user = new User();
            user.setRole(Role.ADMIN);
            user.setName("TestName" + i);
            user.setSurname("TestSurname" +  i);
            user.setPatronymic("TestPatronymic" + i);
            user.setLogin("TestLogin" + i);
            user.setPassword("TestPassword" + i);
            users.add(user);
        }
        userRepository.saveAll(users);
    }

    @org.junit.jupiter.api.Order(1)
    @Test
    void test10000_users() {
        int actual = 10000;
        createOnlyOrders(actual);

        int expected = userRepository.findAll().size();

        Assertions.assertEquals(expected, actual);

        long start = System.nanoTime();
        User user = userRepository.findByLogin("TestLogin" + (actual - 1)).orElse(null);
        long time = System.nanoTime() - start;

        System.out.println("///////////////////////////////////");
        System.out.println("Время выполнения запроса:");
        System.out.println(time/1_000_000_000.0 + " секунд");
        System.out.println("///////////////////////////////////");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test10000_users\nВремя выполнения запроса: " + time_count + " секунд\n";

        appendToFile(out);
    }

    @org.junit.jupiter.api.Order(2)
    @Test
    void test100000_users() {
        int actual = 100000;
        createOnlyOrders(actual);

        int expected = userRepository.findAll().size();

        Assertions.assertEquals(expected, actual);

        long start = System.nanoTime();
        User user = userRepository.findByLogin("TestLogin" + (actual - 1)).orElse(null);
        long time = System.nanoTime() - start;

        System.out.println("///////////////////////////////////");
        System.out.println("Время выполнения запроса:");
        System.out.println(time/1_000_000_000.0 + " секунд");
        System.out.println("///////////////////////////////////");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test100000_users\nВремя выполнения запроса: " + time_count + " секунд\n";

        appendToFile(out);
    }

    @org.junit.jupiter.api.Order(3)
    @Test
    void test1000000_users() {
        int actual = 1000000;
        createOnlyOrders(actual);

        int expected = userRepository.findAll().size();

        Assertions.assertEquals(expected, actual);

        long start = System.nanoTime();
        User user = userRepository.findByLogin("TestLogin" + (actual - 1)).orElse(null);
        long time = System.nanoTime() - start;

        System.out.println("///////////////////////////////////");
        System.out.println("Время выполнения запроса:");
        System.out.println(time/1_000_000_000.0 + " секунд");
        System.out.println("///////////////////////////////////");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test1000000_users\nВремя выполнения запроса: " + time_count + " секунд\n";

        appendToFile(out);

    }

    @org.junit.jupiter.api.Order(4)
    @Test
    void test2000000_users() {
        int actual = 2000000;
        createOnlyOrders(actual);

        int expected = userRepository.findAll().size();

        Assertions.assertEquals(expected, actual);

        long start = System.nanoTime();
        User user = userRepository.findByLogin("TestLogin" + (actual - 1)).orElse(null);
        long time = System.nanoTime() - start;

        System.out.println("///////////////////////////////////");
        System.out.println("Время выполнения запроса:");
        System.out.println(time/1_000_000_000.0 + " секунд");
        System.out.println("///////////////////////////////////");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test2000000_users\nВремя выполнения запроса: " + time_count + " секунд\n";

        appendToFile(out);

    }

    @org.junit.jupiter.api.Order(5)
    @Test
    void test3000000_users() {
        int actual = 3000000;
        createOnlyOrders(actual);

        int expected = userRepository.findAll().size();

        Assertions.assertEquals(expected, actual);

        long start = System.nanoTime();
        User user = userRepository.findByLogin("TestLogin" + (actual - 1)).orElse(null);
        long time = System.nanoTime() - start;

        System.out.println("///////////////////////////////////");
        System.out.println("Время выполнения запроса:");
        System.out.println(time/1_000_000_000.0 + " секунд");
        System.out.println("///////////////////////////////////");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test3000000_users\nВремя выполнения запроса: " + time_count + " секунд\n";

        appendToFile(out);
    }

    @org.junit.jupiter.api.Order(6)
    @Test
    void test10000CreateOrderPipeline() {
        int actual = 10000;
        createOrderPipeline(actual);

        int expected = userRepository.findAll().size();
        int expectedOrders = orderRepository.findAll().size();
        int expectedItems = itemRepository.findAll().size();
        int expectedOrderItems = orderItemRepository.findAll().size();

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expectedOrders, actual);
        Assertions.assertEquals(expectedItems, actual);
        Assertions.assertEquals(expectedOrderItems, actual);

        long start = System.nanoTime();
        User user = userRepository.findByLogin("TestLogin" + (actual - 1)).orElse(null);
        long time = System.nanoTime() - start;

        System.out.println("///////////////////////////////////");
        System.out.println("Время выполнения запроса:");
        System.out.println(time/1_000_000_000.0 + " секунд");
        System.out.println("///////////////////////////////////");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test10000CreateOrderPipeline\nВремя выполнения запроса: " + time_count + " секунд\n";

        appendToFile(out);

    }

    @org.junit.jupiter.api.Order(7)
    @Test
    void test100000CreateOrderPipeline() {
        int actual = 100000;
        createOrderPipeline(actual);

        int expected = userRepository.findAll().size();
        int expectedOrders = orderRepository.findAll().size();
        int expectedItems = itemRepository.findAll().size();
        int expectedOrderItems = orderItemRepository.findAll().size();

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expectedOrders, actual);
        Assertions.assertEquals(expectedItems, actual);
        Assertions.assertEquals(expectedOrderItems, actual);

        long start = System.nanoTime();
        User user = userRepository.findByLogin("TestLogin" + (actual - 1)).orElse(null);
        long time = System.nanoTime() - start;

        System.out.println("///////////////////////////////////");
        System.out.println("Время выполнения запроса:");
        System.out.println(time/1_000_000_000.0 + " секунд");
        System.out.println("///////////////////////////////////");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test100000CreateOrderPipeline\nВремя выполнения запроса: " + time_count + " секунд\n";

        appendToFile(out);

    }

    @org.junit.jupiter.api.Order(8)
    @Test
    void test1000000CreateOrderPipeline() {
        int actual = 1000000;
        createOrderPipeline(actual);

        int expected = userRepository.findAll().size();
        int expectedOrders = orderRepository.findAll().size();
        int expectedItems = itemRepository.findAll().size();
        int expectedOrderItems = orderItemRepository.findAll().size();

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expectedOrders, actual);
        Assertions.assertEquals(expectedItems, actual);
        Assertions.assertEquals(expectedOrderItems, actual);

        long start = System.nanoTime();
        User user = userRepository.findByLogin("TestLogin" + (actual - 1)).orElse(null);
        long time = System.nanoTime() - start;

        System.out.println("///////////////////////////////////");
        System.out.println("Время выполнения запроса:");
        System.out.println(time/1_000_000_000.0 + " секунд");
        System.out.println("///////////////////////////////////");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test1000000CreateOrderPipeline\nВремя выполнения запроса: " + time_count + " секунд\n";

        appendToFile(out);
    }

    private static void appendToFile(String data) {
        try
        {
            String filename= "/home/igorkinev/output.txt";
            FileWriter fw = new FileWriter(filename,true); //the true will append the new data
            fw.write(data);//appends the string to the file
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }



    @org.junit.jupiter.api.Order(9)
    @Test
    void test2000000CreateOrderPipeline() {
        int actual = 2000000;
        createOrderPipeline(actual);

        int expected = userRepository.findAll().size();
        int expectedOrders = orderRepository.findAll().size();
        int expectedItems = itemRepository.findAll().size();
        int expectedOrderItems = orderItemRepository.findAll().size();

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expectedOrders, actual);
        Assertions.assertEquals(expectedItems, actual);
        Assertions.assertEquals(expectedOrderItems, actual);

        long start = System.nanoTime();
        User user = userRepository.findByLogin("TestLogin1").orElse(null);
        long time = System.nanoTime() - start;

        System.out.println("///////////////////////////////////");
        System.out.println("Время выполнения запроса:");
        System.out.println(time/1_000_000_000.0 + " секунд");
        System.out.println("///////////////////////////////////");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test2000000CreateOrderPipeline\nВремя выполнения запроса: " + time_count + " секунд\n";

        appendToFile(out);
    }

    @org.junit.jupiter.api.Order(10)
    @Test
    void test3000000CreateOrderPipeline() {
        int actual = 3000000;
        createOrderPipeline(actual);

        int expected = userRepository.findAll().size();
        int expectedOrders = orderRepository.findAll().size();
        int expectedItems = itemRepository.findAll().size();
        int expectedOrderItems = orderItemRepository.findAll().size();

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expectedOrders, actual);
        Assertions.assertEquals(expectedItems, actual);
        Assertions.assertEquals(expectedOrderItems, actual);

        long start = System.nanoTime();
        User user = userRepository.findByLogin("TestLogin1").orElse(null);
        long time = System.nanoTime() - start;

        System.out.println("///////////////////////////////////");
        System.out.println("Время выполнения запроса:");
        System.out.println(time/1_000_000_000.0 + " секунд");
        System.out.println("///////////////////////////////////");
        String time_count = Double.toString((double) (time/1_000_000_000.0));
        String out = "test3000000CreateOrderPipeline\nВремя выполнения запроса: " + time_count + " секунд\n";

        appendToFile(out);
    }

}
