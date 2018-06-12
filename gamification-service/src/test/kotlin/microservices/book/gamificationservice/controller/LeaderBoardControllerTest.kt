package microservices.book.gamificationservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import microservices.book.gamificationservice.domain.LeaderBoardRow
import microservices.book.gamificationservice.service.GameService
import microservices.book.gamificationservice.service.LeaderBoardService
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@WebMvcTest(LeaderBoardController::class)
class LeaderBoardControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var leaderBoardService: LeaderBoardService

    private lateinit var json: JacksonTester<List<LeaderBoardRow>>

    @Before
    fun setUp() {
        JacksonTester.initFields(this, ObjectMapper())
    }

    @Test
    fun getLeaderBoard() {
        val leaders = listOf(
                LeaderBoardRow(2L, 10),
                LeaderBoardRow(4L, 20)
        )
        given(leaderBoardService.getCurrentLeaderBoard()).willReturn(leaders)

        mockMvc.perform(get("/leaders")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().json(json.write(leaders).json))
    }
}