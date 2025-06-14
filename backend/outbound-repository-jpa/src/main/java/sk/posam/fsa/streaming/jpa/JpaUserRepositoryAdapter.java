package sk.posam.fsa.streaming.jpa;

import org.springframework.stereotype.Repository;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaUserRepositoryAdapter implements UserRepository {

    private final UserSpringDataRepository userSpringDataRepository;

    public JpaUserRepositoryAdapter(UserSpringDataRepository userSpringDataRepository) {
        this.userSpringDataRepository = userSpringDataRepository;
    }

    @Override
    public Optional<User> get(Long id) {
        return userSpringDataRepository.findById(id);
    }

    @Override
    public User create(User user) {
        return userSpringDataRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userSpringDataRepository.save(user);
    }

    @Override
    public Optional<User> findByKeycloakId(UUID keycloakId) {
        return userSpringDataRepository.findByKeycloakId(keycloakId);
    }
}
