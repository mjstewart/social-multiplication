package microservices.book.gamificationservice.service

import microservices.book.gamificationservice.domain.LeaderBoardRow

interface LeaderBoardService {

    /**
     * Retrieves the current leader board with the top score users.
     */
    fun getCurrentLeaderBoard(): List<LeaderBoardRow>
}