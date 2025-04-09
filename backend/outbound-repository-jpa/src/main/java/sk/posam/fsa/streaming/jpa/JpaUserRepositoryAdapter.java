package sk.posam.fsa.streaming.jpa;

import org.springframework.stereotype.Repository;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.repositories.UserRepository;

import java.util.Optional;

@Repository
public class JpaUserRepositoryAdapter implements UserRepository {

    private final UserSpringDataRepository userSpringDataRepository;

    public JpaUserRepositoryAdapter(UserSpringDataRepository userSpringDataRepository) {
        this.userSpringDataRepository = userSpringDataRepository;
    }

    @Override
    public Optional<User> get(Long id) {
        Optional<User> user = userSpringDataRepository.findById(id);
        return user;
    }

    @Override
    public User create(User user) {
        return userSpringDataRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userSpringDataRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmailOrPhoneNumber(String emailOrPhone) {
        return userSpringDataRepository.findByEmail(emailOrPhone)
                .or(() -> userSpringDataRepository.findByPhoneNumber(emailOrPhone));
    }
}
