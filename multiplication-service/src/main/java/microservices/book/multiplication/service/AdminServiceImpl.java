package microservices.book.multiplication.service;

import microservices.book.multiplication.repository.MultiplicationRepository;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class AdminServiceImpl implements AdminService {

    private MultiplicationRepository multiplicationRepository;
    private MultiplicationResultAttemptRepository attemptRepository;
    private UserRepository userRepository;

    public AdminServiceImpl(MultiplicationRepository multiplicationRepository,
                            MultiplicationResultAttemptRepository attemptRepository,
                            UserRepository userRepository) {
        this.multiplicationRepository = multiplicationRepository;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void deleteDatabaseContents() {
        multiplicationRepository.deleteAll();
        attemptRepository.deleteAll();
        userRepository.deleteAll();
    }
}
