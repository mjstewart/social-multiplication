package microservices.book.gamificationservice.event

import microservices.book.gamificationservice.service.GameService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EventHandlerTest {

    @Mock
    private lateinit var gameService: GameService

    @InjectMocks
    private lateinit var eventHandler: EventHandler

    @Test
    fun `handle multiplication solved event`() {
        // given
        val userId = 5L
        val attemptId = 12L
        val correct = true
        val event = MultiplicationSolvedEvent(attemptId, userId, correct)

        // when
        eventHandler.handleMultiplicationSolved(event)

        // then
        verify(gameService).newAttemptForUser(eq(userId), eq(attemptId), eq(correct))
    }
}