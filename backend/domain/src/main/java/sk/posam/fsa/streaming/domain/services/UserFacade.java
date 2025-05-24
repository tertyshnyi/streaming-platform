package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserFacade {
    User createWithKeycloak(User user);
    User updateWithKeycloak(User user);
    Optional<User> get(Long id);
    User create(User user);
    User update(User user);
    void delete(Long id);
}
