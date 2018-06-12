package microservices.book.gamificationservice.service

import microservices.book.gamificationservice.domain.LeaderBoardRow
import microservices.book.gamificationservice.repository.ScoreCardRepository
import org.junit.Test

import org.assertj.core.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LeaderBoardServiceImplTest {

    @Mock
    private lateinit var scoreCardRepository: ScoreCardRepository

    @InjectMocks
    private lateinit var leaderBoardService: LeaderBoardServiceImpl

    @Test
    fun getCurrentLeaderBoard() {
        val expectedLeaderBoard = listOf(LeaderBoardRow(4L, 300))
        given(scoreCardRepository.findTop10()).willReturn(expectedLeaderBoard)

        assertThat(leaderBoardService.getCurrentLeaderBoard()).isEqualTo(expectedLeaderBoard)
    }
}