package sk.posam.fsa.streaming.domain.repositories;

import sk.posam.fsa.streaming.domain.models.entities.User;

public interface KeycloakRegistration {
    String register(User user);

    User update(User user);
}

