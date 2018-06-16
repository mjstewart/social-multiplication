package microservices.book.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import microservices.book.utils.dto.*;
import microservices.book.utils.http.HttpUtils;
import org.junit.Before;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

public class MultiplicationApplication {

    private static final String APPLICATION_BASE_URL = "http://localhost:8000/api";
    private static final String CONTEXT_ATTEMPTS = "/results";
    private static final String CONTEXT_SCORE = "/scores/";
    private static final String CONTEXT_STATS = "/stats";
    private static final String CONTEXT_USERS = "/users/";
    private static final String CONTEXT_LEADERBOARD = "/leaders";
    private static final String CONTEXT_DELETE_DATA_GAM = "/gamification/admin/delete-db";
    private static final String CONTEXT_DELETE_DATA_MULT = "/multiplication/admin/delete-db";

    private HttpUtils httpUtils;

    private ObjectMapper objectMapper;

    public MultiplicationApplication() {
        httpUtils = new HttpUtils(APPLICATION_BASE_URL);

        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Before
    public void clearDatabase() {
        deleteData();
    }

    public MultiplicationResultAttemptResponse sendAttempt(String userAlias, Multiplication multiplication, int result) {
        MultiplicationResultAttemptRequest attempt = new MultiplicationResultAttemptRequest(
                new User(null, userAlias), multiplication, result, false);

        try {
            String jsonResponse = httpUtils.post(CONTEXT_ATTEMPTS, objectMapper.writeValueAsString(attempt));
            return objectMapper.readValue(jsonResponse, MultiplicationResultAttemptResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ScoreCard> getScoreForAttempt(long attemptId) {
        try {
            String jsonResponse = httpUtils.get(CONTEXT_SCORE + attemptId);
            if (jsonResponse.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(jsonResponse, ScoreCard.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GameStat getStatsForUser(long userId) {
        try {
            String jsonResponse = httpUtils.get(CONTEXT_STATS + "?userId=" + userId);
            return objectMapper.readValue(jsonResponse, GameStat.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(long userId) {
        try {
            String jsonResponse = httpUtils.get(CONTEXT_USERS + userId);
            return objectMapper.readValue(jsonResponse, User.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<LeaderBoardRow> getLeaderboard() {
        try {
            String jsonResponse = httpUtils.get(CONTEXT_LEADERBOARD);
            CollectionType type = objectMapper.getTypeFactory().constructCollectionType(List.class, LeaderBoardRow.class);
            return objectMapper.readValue(jsonResponse, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteData() {
        try {
            httpUtils.post(CONTEXT_DELETE_DATA_MULT, "");
            httpUtils.post(CONTEXT_DELETE_DATA_GAM, "");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
