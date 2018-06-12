package microservices.book.gamificationservice.controller

import microservices.book.gamificationservice.domain.LeaderBoardRow
import microservices.book.gamificationservice.service.LeaderBoardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaders")
class LeaderBoardController(
        val leaderBoardService: LeaderBoardService
) {
    @GetMapping
    fun getLeaderBoard(): ResponseEntity<List<LeaderBoardRow>> {
        return ResponseEntity.ok(leaderBoardService.getCurrentLeaderBoard())
    }
}