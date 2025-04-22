package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserFacade {
    Optional<User> get(UUID id);
    User create(User user);
    void delete(UUID id);
}
