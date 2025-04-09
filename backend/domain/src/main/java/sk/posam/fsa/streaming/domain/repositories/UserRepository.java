package sk.posam.fsa.streaming.domain.repositories;

import sk.posam.fsa.streaming.domain.models.entities.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> get(Long id);
    User create(User user);
    void delete(Long id);
}
