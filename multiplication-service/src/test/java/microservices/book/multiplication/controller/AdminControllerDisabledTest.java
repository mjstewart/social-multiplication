package microservices.book.multiplication.controller;

import microservices.book.multiplication.service.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
public class AdminControllerDisabledTest {

    @MockBean
    private AdminService adminService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deleteDatabase() throws Exception {
        mockMvc.perform(post("/multiplication/admin/delete-db")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verifyZeroInteractions(adminService);
    }
}
