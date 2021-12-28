package b22.metro2033.functional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-users-for-auth-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-users-for-auth-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void testUnauthorized() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/"))
                .andExpect(redirectedUrl("http://localhost/auth/login"));

    }

    @Test
    void testForbidden() throws Exception {
        mockMvc
                .perform(post("/auth/login").param("user", "Test"))
                .andExpect(redirectedUrl("/auth/login?error"));

    }

    @Test
    @WithMockUser(username = "a", password = "ggg")
    void loginAsAdmin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "gen", password = "ggg")
    void loginAsGeneral() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "s", password = "ggg")
    void loginAsSoldier() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "hc", password = "ggg", authorities = "delivery:read")
    void testOfShowingAllOrders() throws Exception {

        mockMvc.perform(get("/delivery"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "c", password = "ggg")
    void loginAsCourier() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "gue", password = "ggg")
    void loginAsGuest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }
}