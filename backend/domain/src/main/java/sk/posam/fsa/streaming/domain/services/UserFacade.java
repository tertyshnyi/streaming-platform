package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.User;

import java.util.Optional;

public interface UserFacade {
    Optional<User> get(Long id);
    User create(User user);
    void delete(Long id);
}
