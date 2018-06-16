package microservices.book.utils.dto;

public class LeaderBoardRow {
    private Long userId;
    private Long totalScore;

    public LeaderBoardRow() {
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTotalScore() {
        return totalScore;
    }
}