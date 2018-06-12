package microservices.book.gamificationservice.repository

import microservices.book.gamificationservice.domain.BadgeCard
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BadgeCardRepository : CrudRepository<BadgeCard, Long> {
    /**
     * Get all newlyAwardedBadges for the given {@code userId}
     */
    fun findByUserIdOrderByBadgeDescTimestampDesc(userId: Long): List<BadgeCard>
}

