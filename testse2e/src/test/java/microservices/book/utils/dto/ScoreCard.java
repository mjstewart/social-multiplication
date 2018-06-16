package microservices.book.utils.dto;

public class ScoreCard {
    private long userId;
    private long attemptId;
    private int score;
    private long timestamp;

    public ScoreCard(long userId, long attemptId, int score, long timestamp) {
        this.userId = userId;
        this.attemptId = attemptId;
        this.score = score;
        this.timestamp = timestamp;
    }

    public ScoreCard() {
    }

    public long getUserId() {
        return userId;
    }

    public long getAttemptId() {
        return attemptId;
    }

    public int getScore() {
        return score;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
