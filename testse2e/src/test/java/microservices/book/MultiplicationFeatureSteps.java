package microservices.book;

import cucumber.api.java8.En;
import microservices.book.utils.MultiplicationApplication;
import microservices.book.utils.dto.GameStat;
import microservices.book.utils.dto.Multiplication;
import microservices.book.utils.dto.MultiplicationResultAttemptResponse;
import microservices.book.utils.dto.ScoreCard;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiplicationFeatureSteps implements En {

    private MultiplicationApplication app = new MultiplicationApplication();
    private MultiplicationResultAttemptResponse lastAttemptResponse;
    private GameStat lastGameStatResponse;

    public MultiplicationFeatureSteps() {

        When("the user ([^\\s]+) sends (\\d+) ([^\\s]+) attempts",
                (String userAlias, Integer attempts, String outcome) -> {
                    // Write code here that turns the phrase above into concrete actions
                    int correctAttemptsSent = 0;
                    boolean isCorrect = "right".equals(outcome);

                    Multiplication multiplication = new Multiplication(10, 10);
                    int correctResult = 100;
                    int incorrectResult = 258;
                    for (int i = 0; i < attempts; i++) {
                        MultiplicationResultAttemptResponse attempt = app.sendAttempt(userAlias, multiplication,
                                isCorrect ? correctResult : incorrectResult);
                        lastAttemptResponse = attempt;
                        correctAttemptsSent += attempt.isCorrect() ? 1 : 0;
                    }

                    assertThat(correctAttemptsSent)
                            .isEqualTo(isCorrect ? attempts : 0)
                            .withFailMessage("Error sending attempts to the application");
                });

        Then("the user gets a response indicating the attempt is ([^\\s]+)", (String outcome) -> {
            // Write code here that turns the phrase above into concrete actions
            assertThat(lastAttemptResponse.isCorrect())
                    .isEqualTo("right".equals(outcome))
                    .withFailMessage("Expecting a response with a " + outcome + " attempt");
        });

        Then("the user gets (\\d+) points for the attempt", (Integer points) -> {
            // Write code here that turns the phrase above into concrete actions
            Long id = lastAttemptResponse.getId();

            // wait for event to propagate to other microservices since messages travel through rabbitmq
            Thread.sleep(2000);

            Integer score = app.getScoreForAttempt(id).map(ScoreCard::getScore).orElse(0);
            assertThat(score).isEqualTo(points);
        });

        Then("the user gets the ([^\\s]+) badge", (String badgeType) -> {
            // Write code here that turns the phrase above into concrete actions
            long userId = lastAttemptResponse.getUser().getId();
            Thread.sleep(2000);
            lastGameStatResponse = app.getStatsForUser(userId);
            List<String> userBadges = lastGameStatResponse.getBadges();
            assertThat(userBadges).contains(badgeType);
        });

        Then("the user does not get any badge", () -> {
            long userId = lastAttemptResponse.getUser().getId();
            GameStat stats = app.getStatsForUser(userId);
            List<String> userBadges = stats.getBadges();
            if (stats.getScore() == 0) {
                assertThat(userBadges).isNullOrEmpty();
            } else {
                assertThat(userBadges).isEqualTo(lastGameStatResponse.getBadges());
            }
        });
    }
}
