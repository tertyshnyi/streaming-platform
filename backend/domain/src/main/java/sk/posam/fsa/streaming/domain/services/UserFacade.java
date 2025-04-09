package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.User;

import java.util.Optional;

public interface UserFacade {
    Optional<User> get(Long requestedId, Long currentUserId);
    User create(User user);
    void delete(Long requestedId, Long currentUserId);
    String login(String emailOrPhone, String password);
}
