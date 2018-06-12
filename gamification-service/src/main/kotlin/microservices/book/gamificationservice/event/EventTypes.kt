package microservices.book.gamificationservice.event

/**
 * Don't forget to give default values so a no-arg constructor is created for jackson.
 */
data class MultiplicationSolvedEvent(
        val multiplicationResultAttemptId: Long = 0,
        val userId: Long = 0,
        val correct: Boolean = false
)