package microservices.book.utils.dto;

import java.util.List;

public class GameStat {
    private long userId;
    private List<String> badges;
    private int score;

    public GameStat(long userId, List<String> badges, int score) {
        this.userId = userId;
        this.badges = badges;
        this.score = score;
    }

    public GameStat() {
    }

    public long getUserId() {
        return userId;
    }

    public List<String> getBadges() {
        return badges;
    }

    public int getScore() {
        return score;
    }
}
