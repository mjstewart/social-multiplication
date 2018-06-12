package microservices.book.gamificationservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import microservices.book.gamificationservice.domain.GameStat
import microservices.book.gamificationservice.service.GameService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@WebMvcTest(UserStatsController::class)
class UserStatsControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var gameService: GameService

    private lateinit var json: JacksonTester<GameStat>

    @Before
    fun setUp() {
        JacksonTester.initFields(this, ObjectMapper())
    }

    @Test
    fun `get stats for user - userId query param must be provided`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/stats?blah"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `get stats for user - userId must be a number`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/stats?userId=abc"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `get stats for existing user`() {
        val userId = 5L
        val stats = GameStat(userId, 5)
        given(gameService.retrieveStatsForUser(userId)).willReturn(stats)

        mockMvc.perform(MockMvcRequestBuilders.get("/stats?userId=$userId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().json(json.write(stats).json))
    }

    @Test
    fun `get stats for non existing user returns 404 not found`() {
        val userId = 5L
        given(gameService.retrieveStatsForUser(userId)).willReturn(null)

        mockMvc.perform(MockMvcRequestBuilders.get("/stats?userId=$userId"))
                .andExpect(status().isNotFound)
    }

}