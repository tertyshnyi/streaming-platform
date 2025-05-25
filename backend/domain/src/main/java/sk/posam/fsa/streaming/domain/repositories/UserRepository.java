package sk.posam.fsa.streaming.domain.repositories;

import sk.posam.fsa.streaming.domain.models.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> get(Long id);
    User create(User user);
    User update(User user);
    Optional<User> findByKeycloakId(UUID keycloakId);
}
