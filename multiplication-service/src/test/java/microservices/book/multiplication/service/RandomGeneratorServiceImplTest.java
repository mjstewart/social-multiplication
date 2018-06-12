package microservices.book.multiplication.service;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

public class RandomGeneratorServiceImplTest {

    private RandomGeneratorServiceImpl randomGeneratorService;

    @Before
    public void setUp() {
        randomGeneratorService = new RandomGeneratorServiceImpl();
    }

    @Test
    public void generateRandomFactor_IsBetweenExpectedLimits() {
        List<Integer> randomFactors = IntStream.range(0, 1000)
                .map(i -> randomGeneratorService.generateRandomFactor())
                .boxed().collect(Collectors.toList());

        // all randomly generated factors must be within the range of 11 - 99.
        assertThat(randomFactors).containsOnlyElementsOf(IntStream.rangeClosed(11, 99).boxed()
                .collect(Collectors.toList()));
    }
}
