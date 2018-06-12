package microservices.book.multiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.service.MultiplicationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {

    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<MultiplicationResultAttempt> jsonResultAttempt;
    private JacksonTester<List<MultiplicationResultAttempt>> jsonResultAttemptList;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void postCorrectAttempt() throws Exception {
        postAttempt(true);
    }

    @Test
    public void postIncorrectAttempt() throws Exception {
        postAttempt(false);
    }

    private void postAttempt(boolean correct) throws Exception {
        MultiplicationResultAttempt expectedVerifiedAttempt = new MultiplicationResultAttempt(new User("john"),
                new Multiplication(50, 70), 3500, correct);

        given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class))).willReturn(expectedVerifiedAttempt);

        mockMvc.perform(post("/results")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonResultAttempt.write(expectedVerifiedAttempt).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResultAttempt.write(expectedVerifiedAttempt).getJson()));
    }

    @Test
    public void getUserStats() throws Exception {
        User user = new User("john");
        Multiplication multiplication = new Multiplication(50, 60);

        List<MultiplicationResultAttempt> attempts = List.of(
                new MultiplicationResultAttempt(user, multiplication, 3010, false),
                new MultiplicationResultAttempt(user, multiplication, 3005, false)
        );
        given(multiplicationService.getStatsForUser("john")).willReturn(attempts);

        mockMvc.perform(get("/results?alias=john")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResultAttemptList.write(attempts).getJson()));
    }

    @Test
    public void getResultAttempt_IllegalAttemptId_BadRequest() throws Exception {
        mockMvc.perform(get("/results/blah")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getResultAttempt_AttemptIdNotFound_404() throws Exception {
        Long attemptId = 4L;
        given(multiplicationService.getResultAttempt(attemptId)).willReturn(Optional.empty());

        mockMvc.perform(get("/results/4")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getResultAttempt_AttemptIdFound_ReturnsResult() throws Exception {
        Long attemptId = 4L;

        var resultAttempt = new MultiplicationResultAttempt(new User("bob"),
                new Multiplication(5, 7), 35, true);
        resultAttempt.setId(attemptId);

        given(multiplicationService.getResultAttempt(attemptId)).willReturn(Optional.of(resultAttempt));

        mockMvc.perform(get("/results/4")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResultAttempt.write(resultAttempt).getJson()));
    }
}