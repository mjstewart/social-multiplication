package microservices.book.gamificationservice.service

import microservices.book.gamificationservice.client.MultiplicationResultAttemptClient
import microservices.book.gamificationservice.domain.Badge
import microservices.book.gamificationservice.domain.BadgeCard
import microservices.book.gamificationservice.domain.GameStat
import microservices.book.gamificationservice.domain.ScoreCard
import microservices.book.gamificationservice.repository.BadgeCardRepository
import microservices.book.gamificationservice.repository.ScoreCardRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service

@Service
class GameServiceImpl(
        val badgeCardRepository: BadgeCardRepository,
        val scoreCardRepository: ScoreCardRepository,
        val attemptClient: MultiplicationResultAttemptClient
) : GameService {

    companion object {
        val logger = LoggerFactory.getLogger(GameServiceImpl::class.java)!!

        const val BRONZE_THRESHOLD = 100
        const val SILVER_THRESHOLD = 500
        const val GOLD_THRESHOLD = 999
        const val LUCKY_NUMBER = 42
    }

    override fun newAttemptForUser(userId: Long, attemptId: Long, correct: Boolean): GameStat {
        if (correct) {
            ScoreCard(userId, attemptId).let {
                scoreCardRepository.save(it)
                logger.info("User with id {} scored {} points for attempt id {}", userId, it.score, attemptId)
                val newlyAwardedBadges = processBadges(userId, attemptId).map { it.badge }.toSet()
                return GameStat(userId, it.score, newlyAwardedBadges)
            }
        }
        logger.info("Incorrect attempt for User with id {} and attemptId {}", userId, attemptId)
        return GameStat(userId)
    }


    override fun retrieveStatsForUser(userId: Long): GameStat? {
        return try {
            val score = scoreCardRepository.getTotalScoreForUser(userId)

            val badges = badgeCardRepository.findByUserIdOrderByBadgeDescTimestampDesc(userId)
                    .map { it.badge }.toSet()
            GameStat(userId, score, badges)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    /**
     * Depending on the users total score, assign the appropriate badge if they don't already have it.
     *
     * Only the newly awarded newlyAwardedBadges are returned.
     */
    private fun processBadges(userId: Long, attemptId: Long): List<BadgeCard> {
        val existingBadgeCards = badgeCardRepository.findByUserIdOrderByBadgeDescTimestampDesc(userId)
        val scoreCards = scoreCardRepository.findByUserIdOrderByScoreDescTimestampDesc(userId)
        val totalScore = scoreCardRepository.getTotalScoreForUser(userId)

        logger.info("New score for user {} is {}", userId, totalScore)

        val newBadgeCards = arrayListOf<BadgeCard>()
        val badgeMaker: (Badge) -> BadgeCard = { badge -> BadgeCard(userId, badge) }

        checkAndGiveBadge(totalScore, BRONZE_THRESHOLD, existingBadgeCards, badgeMaker(Badge.BRONZE))
                ?.also { newBadgeCards.add(it) }
        checkAndGiveBadge(totalScore, SILVER_THRESHOLD, existingBadgeCards, badgeMaker(Badge.SILVER))
                ?.also { newBadgeCards.add(it) }
        checkAndGiveBadge(totalScore, GOLD_THRESHOLD, existingBadgeCards, badgeMaker(Badge.GOLD))
                ?.also { newBadgeCards.add(it) }

        // this method is called when attempt is correct, hence no check is done here.
        if (scoreCards.size == 1 && existingBadgeCards.none { it.badge == Badge.FIRST_WIN }) {
            giveBadgeToUser(badgeMaker(Badge.FIRST_WIN)).also { newBadgeCards.add(it) }
        }

        if (existingBadgeCards.none { it.badge == Badge.LUCKY_NUMBER }) {
            logger.info("Calling multiplication microservice to check for lucky number")
            attemptClient.retrieveMultiplicationResultAttemptById(attemptId)?.apply {
                if (multiplicationFactorA == LUCKY_NUMBER || multiplicationFactorB == LUCKY_NUMBER) {
                    giveBadgeToUser(badgeMaker(Badge.LUCKY_NUMBER)).also { newBadgeCards.add(it) }
                    logger.info("User {} has been awarded the lucky number badge - a factor contains {}", userId, LUCKY_NUMBER)
                } else {
                    logger.info("User {} has NOT been awarded the lucky number badge seems no factor contains {}", userId, LUCKY_NUMBER)
                }
            }
        }
        return newBadgeCards
    }

    private fun checkAndGiveBadge(totalScore: Int, threshold: Int,
                                  badges: List<BadgeCard>, card: BadgeCard): BadgeCard? {
        if (totalScore >= threshold && badges.none { it.badge == card.badge }) {
            return giveBadgeToUser(card)
        }
        return null
    }

    private fun giveBadgeToUser(card: BadgeCard): BadgeCard {
        return badgeCardRepository.save(card).also {
            logger.info("User with id {} won a new badge: {}", it.userId, it.badge)
        }
    }

    override fun getScoreForAttempt(attemptId: Long): ScoreCard? {
        return scoreCardRepository.findByAttemptId(attemptId)
    }
}