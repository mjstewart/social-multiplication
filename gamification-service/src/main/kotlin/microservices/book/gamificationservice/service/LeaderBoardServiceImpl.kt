package microservices.book.gamificationservice.service

import microservices.book.gamificationservice.domain.LeaderBoardRow
import microservices.book.gamificationservice.repository.ScoreCardRepository
import org.springframework.stereotype.Service

@Service
class LeaderBoardServiceImpl(val scoreCardRepository: ScoreCardRepository) : LeaderBoardService {

    override fun getCurrentLeaderBoard(): List<LeaderBoardRow> {
        return scoreCardRepository.findTop10()
    }
}