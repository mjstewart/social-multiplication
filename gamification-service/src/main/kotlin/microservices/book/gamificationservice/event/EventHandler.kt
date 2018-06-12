package microservices.book.gamificationservice.event

import microservices.book.gamificationservice.service.GameService
import microservices.book.gamificationservice.service.GameServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class EventHandler(val gameService: GameService) {

    companion object {
        val logger = LoggerFactory.getLogger(EventHandler::class.java)!!
    }

    @RabbitListener(queues = ["\${multiplication.queue}"])
    fun handleMultiplicationSolved(event: MultiplicationSolvedEvent) {
        logger.info("MultiplicationSolvedEvent received attemptId: {}", event.multiplicationResultAttemptId)

        try {
            gameService.newAttemptForUser(event.userId, event.multiplicationResultAttemptId, event.correct)
        } catch (e: Exception) {
            logger.error("Error when trying to process MultiplicationSolvedEvent: {}", e)
            throw AmqpRejectAndDontRequeueException(e)
        }
    }
}