package microservices.book.gamificationservice.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

enum class Badge {
    BRONZE,
    SILVER,
    GOLD,

    FIRST_ATTEMPT,
    FIRST_WIN,

    LUCKY_NUMBER
}

@Entity
data class BadgeCard(val userId: Long,
                     val badge: Badge,
                     val timestamp: Long = System.currentTimeMillis()) {
    @field:[Id GeneratedValue JsonIgnore]
    var id: Long? = null
}

@Entity
data class ScoreCard(val userId: Long,
                     val attemptId: Long,
                     val score: Int = DEFAULT_SCORE,
                     val timestamp: Long = System.currentTimeMillis()) {
    @field:[Id GeneratedValue JsonIgnore]
    var id: Long? = null

    companion object {
        const val DEFAULT_SCORE = 10
    }
}

@Entity
data class GameStat(val userId: Long,
                    val score: Int = 0,
                    @field:[ElementCollection Enumerated(EnumType.STRING)]
                     val newlyAwardedBadges: Set<Badge> = setOf()) {
    @field:[Id GeneratedValue JsonIgnore]
    var id: Long? = null
}

data class LeaderBoardRow(val userId: Long, val totalScore: Long)