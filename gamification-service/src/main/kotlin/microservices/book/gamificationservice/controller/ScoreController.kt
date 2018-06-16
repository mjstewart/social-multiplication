package microservices.book.gamificationservice.controller

import microservices.book.gamificationservice.domain.ScoreCard
import microservices.book.gamificationservice.service.GameService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/scores")
class ScoreController(private val gameService: GameService) {

    fun getScoreForAttempt(@PathVariable attemptId: Long): ScoreCard? {
        return gameService.getScoreForAttempt(attemptId)
    }
}