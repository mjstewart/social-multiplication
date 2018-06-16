package microservices.book.gamificationservice.service

import microservices.book.gamificationservice.repository.BadgeCardRepository
import microservices.book.gamificationservice.repository.ScoreCardRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("test")
@Service
class AdminServiceImpl(
        private val badgeCardRepository: BadgeCardRepository,
        private val scoreCardRepository: ScoreCardRepository
) : AdminService {
    override fun deleteDatabaseContents() {
        badgeCardRepository.deleteAll()
        scoreCardRepository.deleteAll()
    }
}