package microservices.book.multiplication.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomGeneratorServiceImpl implements RandomGeneratorService {

    private final static int MIN = 11;
    private final static int MAX = 99;
    private final static Random RANDOM = new Random();

    @Override
    public int generateRandomFactor() {
        return RANDOM.nextInt((MAX - MIN) + 1) + MIN;
    }
}

