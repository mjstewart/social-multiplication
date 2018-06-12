package microservices.book.gamificationservice.repository

import microservices.book.gamificationservice.domain.LeaderBoardRow
import microservices.book.gamificationservice.domain.ScoreCard
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner
import org.assertj.core.api.Assertions.*

@RunWith(SpringRunner::class)
@DataJpaTest
class ScoreCardRepositoryTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var scoreCardRepository: ScoreCardRepository

    @Test
    fun findTop10() {
        val scoreCard1 = ScoreCard(5, 10, 100, 300)
        val scoreCard2 = ScoreCard(6, 11, 100, 200)
        val scoreCard3 = ScoreCard(7, 12, 300, 100)
        val scoreCard4 = ScoreCard(8, 13, 300, 101)
        val scoreCard5 = ScoreCard(8, 15, 70, 105)

        entityManager.persist(scoreCard1)
        entityManager.persist(scoreCard2)
        entityManager.persist(scoreCard3)
        entityManager.persist(scoreCard4)
        entityManager.persist(scoreCard5)

        val leaders = scoreCardRepository.findTop10()
        assertThat(leaders).containsExactly(
                LeaderBoardRow(8, 370),
                LeaderBoardRow(7, 300),
                LeaderBoardRow(5, 100),
                LeaderBoardRow(6, 100)
        )
    }

    @Test
    fun getTotalScoreForUser() {
        val scoreCard1 = ScoreCard(5, 10, 100, 200)
        val scoreCard2 = ScoreCard(5, 11, 200, 300)
        val scoreCard3 = ScoreCard(100, 12, 500, 100)

        entityManager.persist(scoreCard1)
        entityManager.persist(scoreCard2)
        entityManager.persist(scoreCard3)

        assertThat(scoreCardRepository.getTotalScoreForUser(5)).isEqualTo(300)
        assertThat(scoreCardRepository.getTotalScoreForUser(100)).isEqualTo(500)
    }

    @Test
    fun findByUserIdOrderByScoreDescTimestampDesc() {
        val scoreCard1 = ScoreCard(5, 10, 100, 200)
        val scoreCard2 = ScoreCard(5, 11, 200, 301)
        val scoreCard3 = ScoreCard(5, 12, 200, 300)
        val scoreCard4 = ScoreCard(100, 12, 500, 100)

        entityManager.persist(scoreCard1)
        entityManager.persist(scoreCard2)
        entityManager.persist(scoreCard3)
        entityManager.persist(scoreCard4)

        assertThat(scoreCardRepository.findByUserIdOrderByScoreDescTimestampDesc(5))
                .containsExactly(scoreCard2, scoreCard3, scoreCard1)
        assertThat(scoreCardRepository.findByUserIdOrderByScoreDescTimestampDesc(100))
                .containsOnly(scoreCard4)
    }
}