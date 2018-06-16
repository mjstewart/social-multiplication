package microservices.book.gamificationservice.controller

import microservices.book.gamificationservice.service.AdminService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verifyZeroInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RunWith(SpringRunner::class)
@WebMvcTest(AdminController::class)
class AdminControllerDisabledTest {
    @MockBean
    private lateinit var adminService: AdminService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun deleteDatabaseTest() {
        mockMvc.perform(MockMvcRequestBuilders.post("/gamification/admin/delete-db")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound)

        verifyZeroInteractions(adminService)
    }
}