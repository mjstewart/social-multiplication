package microservices.book.gamificationservice.controller

import microservices.book.gamificationservice.domain.GameStat
import microservices.book.gamificationservice.event.EventHandler
import microservices.book.gamificationservice.service.GameService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stats")
class UserStatsController(
        val gameService: GameService
) {
    companion object {
        val logger = LoggerFactory.getLogger(UserStatsController::class.java)!!
    }

    /**
     * Technically its possible for the userId to not exist yet due to order of operations.
     * There could be a delay in receiving the MultiplicationSolvedEvent from another microservice which
     * means this microservice wouldnt have saved the solved event details yet.
     */
    @GetMapping
    fun getStatsForUser(@RequestParam userId: Long): ResponseEntity<GameStat>? {
        logger.info("/stats called for userId: {}", userId)
        return gameService.retrieveStatsForUser(userId)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }
}