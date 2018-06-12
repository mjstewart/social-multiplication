package microservices.book.gamificationservice.service

import microservices.book.gamificationservice.client.MultiplicationResultAttemptClient
import microservices.book.gamificationservice.client.dto.MultiplicationResultAttempt
import microservices.book.gamificationservice.domain.Badge
import microservices.book.gamificationservice.domain.BadgeCard
import microservices.book.gamificationservice.domain.GameStat
import microservices.book.gamificationservice.domain.ScoreCard
import microservices.book.gamificationservice.repository.BadgeCardRepository
import microservices.book.gamificationservice.repository.ScoreCardRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GameServiceImplTest {
    @Mock
    private lateinit var badgeCardRepository: BadgeCardRepository

    @Mock
    private lateinit var scoreCardRepository: ScoreCardRepository

    @Mock
    private lateinit var attemptClient: MultiplicationResultAttemptClient

    @InjectMocks
    private lateinit var gameService: GameServiceImpl

    @Test
    fun `When the first attempt is correct, award the FIRST_WIN badge`() {
        // given
        val userId = 12L
        val attemptId = 1L
        val totalScore = 10

        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore)

        // The user has no awarded badges yet
        given(badgeCardRepository.findByUserIdOrderByBadgeDescTimestampDesc(userId))
                .willReturn(emptyList())

        // The scorecard is saved before newAttemptForUser is called
        given(scoreCardRepository.findByUserIdOrderByScoreDescTimestampDesc(userId))
                .willReturn(createNScoreCards(userId, 1))

        // save returns the passed in argument to simulate returning the persisted object
        given(badgeCardRepository.save(any(BadgeCard::class.java)))
                .willAnswer { invocation -> invocation.getArgument(0) }

        // when
        val stat: GameStat = gameService.newAttemptForUser(userId, attemptId, true)

        // then
        assertThat(stat.score).isEqualTo(totalScore)
        assertThat(stat.newlyAwardedBadges).containsOnly(Badge.FIRST_WIN)

        verify(scoreCardRepository).save(any(ScoreCard::class.java))
        verify(scoreCardRepository).getTotalScoreForUser(userId)
        verify(scoreCardRepository).findByUserIdOrderByScoreDescTimestampDesc(userId)

        verify(badgeCardRepository).findByUserIdOrderByBadgeDescTimestampDesc(userId)
        verify(badgeCardRepository).save(any(BadgeCard::class.java))
    }

    @Test
    fun `When attempt is incorrect, award no badges and do not save ScoreCard`() {
        // given
        val userId = 12L
        val attemptId = 1L

        // when
        val stat = gameService.newAttemptForUser(userId, attemptId, false)

        // then
        assertThat(stat.score).isZero()
        assertThat(stat.newlyAwardedBadges).isEmpty()

        verifyZeroInteractions(scoreCardRepository)
        verifyZeroInteractions(badgeCardRepository)
    }

    @Test
    fun `Do not award the BRONZE badge when the user achieves less than threshold`() {
        badgeThresholdChecker(GameServiceImpl.BRONZE_THRESHOLD - 1)
    }

    @Test
    fun `Award the BRONZE badge when the user achieves exactly the threshold points`() {
        badgeThresholdChecker(GameServiceImpl.BRONZE_THRESHOLD,
                expectedNewlyAwardedBadges = listOf(Badge.BRONZE))
    }

    @Test
    fun `Award the BRONZE badge when the user achieves over the threshold points`() {
        badgeThresholdChecker(GameServiceImpl.BRONZE_THRESHOLD + 1,
                expectedNewlyAwardedBadges = listOf(Badge.BRONZE))
    }

    @Test
    fun `Do not award the SILVER badge when the user achieves less than threshold`() {
        badgeThresholdChecker(GameServiceImpl.SILVER_THRESHOLD - 1,
                expectedNewlyAwardedBadges = listOf(Badge.BRONZE))
    }

    @Test
    fun `Award the SILVER badge when the user achieves exactly the threshold points`() {
        badgeThresholdChecker(GameServiceImpl.SILVER_THRESHOLD,
                expectedNewlyAwardedBadges = listOf(Badge.BRONZE, Badge.SILVER))
    }

    @Test
    fun `Award the SILVER badge when the user achieves over the threshold points`() {
        badgeThresholdChecker(GameServiceImpl.SILVER_THRESHOLD + 1,
                expectedNewlyAwardedBadges = listOf(Badge.BRONZE, Badge.SILVER))
    }

    @Test
    fun `Do not award the GOLD badge when the user achieves less than threshold`() {
        badgeThresholdChecker(GameServiceImpl.GOLD_THRESHOLD - 1,
                expectedNewlyAwardedBadges = listOf(Badge.BRONZE, Badge.SILVER))
    }

    @Test
    fun `Award the GOLD badge when the user achieves exactly the threshold points`() {
        badgeThresholdChecker(GameServiceImpl.GOLD_THRESHOLD,
                expectedNewlyAwardedBadges = listOf(Badge.BRONZE, Badge.SILVER, Badge.GOLD))
    }

    @Test
    fun `Award the GOLD badge when the user achieves over the threshold points`() {
        badgeThresholdChecker(GameServiceImpl.GOLD_THRESHOLD + 1,
                expectedNewlyAwardedBadges = listOf(Badge.BRONZE, Badge.SILVER, Badge.GOLD))
    }

    @Test
    fun `Award no new badges when user already has them all`() {
        badgeThresholdChecker(999, existingBadges = listOf(Badge.BRONZE, Badge.SILVER, Badge.GOLD))
    }


    private fun badgeThresholdChecker(totalScore: Int,
                                      expectedNewlyAwardedBadges: List<Badge> = listOf(),
                                      existingBadges: List<Badge> = listOf()) {
        // given
        val userId = 12L
        val attemptId = 8L

        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore)

        // Create more than 1 score card to prevent FIRST_WIN badge which isn't important to test
        given(scoreCardRepository.findByUserIdOrderByScoreDescTimestampDesc(userId))
                .willReturn(createNScoreCards(userId, 2))

        // The user already has these badges
        given(badgeCardRepository.findByUserIdOrderByBadgeDescTimestampDesc(userId))
                .willReturn(existingBadges.map { BadgeCard(userId, it) })

        // expectedNewlyAwardedBadges should be saved in the exact same order only if the user doesn't own the badge
        val expectedBadgesToBeSaved = expectedNewlyAwardedBadges.subtract(existingBadges)

        // save returns the passed in argument to simulate returning the persisted object
        given(badgeCardRepository.save(any(BadgeCard::class.java)))
                .willAnswer { invocation -> invocation.getArgument(0) }

        // when
        val stat: GameStat = gameService.newAttemptForUser(userId, attemptId, true)

        // then
        assertThat(stat.score).isEqualTo(ScoreCard.DEFAULT_SCORE)

        assertThat(stat.newlyAwardedBadges).containsExactlyInAnyOrderElementsOf(expectedNewlyAwardedBadges.toSet())

        verify(badgeCardRepository, times(expectedBadgesToBeSaved.size)).save(any(BadgeCard::class.java))
    }

    @Test
    fun retrieveStatsForUser() {
        // given
        val userId = 12L

        val scoreCards = createNScoreCards(userId, 5)
        val expectedTotalScore = scoreCards.size * ScoreCard.DEFAULT_SCORE
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(expectedTotalScore)

        // pick any random badge for the purpose of supplying test data.
        val card = BadgeCard(userId, Badge.BRONZE)
        given(badgeCardRepository.findByUserIdOrderByBadgeDescTimestampDesc(userId))
                .willReturn(listOf(card))

        // when
        val stats = gameService.retrieveStatsForUser(userId)

        // then
        assertThat(stats?.score).isEqualTo(expectedTotalScore)
        assertThat(stats?.newlyAwardedBadges).containsOnly(card.badge)
    }

    @Test
    fun `When any factor contains the lucky number, award the LUCKY_NUMBER badge`() {
        // given
        val userId = 12L
        val attemptId = 1L
        val totalScore = 10

        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore)

        // The user has no awarded badges yet
        given(badgeCardRepository.findByUserIdOrderByBadgeDescTimestampDesc(userId))
                .willReturn(emptyList())

        // Create more than 1 score card to prevent FIRST_WIN badge which isn't important to test.
        given(scoreCardRepository.findByUserIdOrderByScoreDescTimestampDesc(userId))
                .willReturn(createNScoreCards(userId, 2))

        // save returns the passed in argument to simulate returning the persisted object
        given(badgeCardRepository.save(any(BadgeCard::class.java)))
                .willAnswer { invocation -> invocation.getArgument(0) }

        // The attempt contains the lucky number
        val attempt = MultiplicationResultAttempt("bob", 10, GameServiceImpl.LUCKY_NUMBER,
                50, false)

        given(attemptClient.retrieveMultiplicationResultAttemptById(attemptId)).willReturn(attempt)

        // when
        val stat: GameStat = gameService.newAttemptForUser(userId, attemptId, true)

        // then
        assertThat(stat.score).isEqualTo(totalScore)
        assertThat(stat.newlyAwardedBadges).containsOnly(Badge.LUCKY_NUMBER)
    }

    private fun createNScoreCards(userId: Long, n: Long): List<ScoreCard> =
            (0 until n).map { ScoreCard(userId, it) }
}