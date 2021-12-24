package b22.metro2033.functional;

import b22.metro2033.Entity.Delivery.Courier;
import b22.metro2033.Repository.Delivery.CourierRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("head_courier")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-for-courier-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-for-courier-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourierRepository courierRepository;


    @Test
    void testOfShowingAllCouriersIndex() throws Exception {
        List<Courier> courierList = courierRepository.findAll();

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

//        Assertions.assertAll(soldierRepository.findAll().orElse(null));
    }
    @Test
    void createForm() {

    }

    @Test
    void create() {
    }

    @Test
    void remove() {
    }
}