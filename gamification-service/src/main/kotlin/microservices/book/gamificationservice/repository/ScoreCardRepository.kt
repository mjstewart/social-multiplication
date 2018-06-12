package microservices.book.gamificationservice.repository

import microservices.book.gamificationservice.domain.LeaderBoardRow
import microservices.book.gamificationservice.domain.ScoreCard
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ScoreCardRepository : CrudRepository<ScoreCard, Long> {

    /**
     * Gets total score for a user identified by userId.
     */
    @Query("SELECT SUM(sc.score)" +
            " FROM microservices.book.gamificationservice.domain.ScoreCard sc" +
            " WHERE sc.userId = :userId" +
            " GROUP BY sc.userId"
    )
    fun getTotalScoreForUser(@Param("userId") userId: Long): Int

    /**
     * Gets the top 10 scores.
     */
    @Query("SELECT NEW microservices.book.gamificationservice.domain.LeaderBoardRow(sc.userId, SUM(sc.score)) " +
            "FROM microservices.book.gamificationservice.domain.ScoreCard sc " +
            "GROUP BY sc.userId " +
            "ORDER BY SUM(sc.score) DESC, MAX(sc.timestamp) DESC"
    )
    fun findTop10(): List<LeaderBoardRow>

    /**
     * Gets all the {@code ScoreCard}s associated with the {@code userId} ordered by highest to lowest
     * score and timestamp.
     */
    fun findByUserIdOrderByScoreDescTimestampDesc(userId: Long): List<ScoreCard>
}