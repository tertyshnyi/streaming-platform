package sk.posam.fsa.streaming.domain.repositories;

import sk.posam.fsa.streaming.domain.models.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> get(UUID id);
    User create(User user);
    void delete(UUID id);
}
