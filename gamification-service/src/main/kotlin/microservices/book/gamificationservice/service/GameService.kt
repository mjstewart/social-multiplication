package microservices.book.gamificationservice.service

import microservices.book.gamificationservice.domain.GameStat
import microservices.book.gamificationservice.domain.ScoreCard

interface GameService {

    /**
     * Process a new attempt for a given user.
     */
    fun newAttemptForUser(userId: Long, attemptId: Long, correct: Boolean): GameStat

    /**
     * Gets the game statistics for a given user.
     */
    fun retrieveStatsForUser(userId: Long): GameStat?

    fun getScoreForAttempt(attemptId: Long): ScoreCard?
}