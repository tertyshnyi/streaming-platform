package sk.posam.fsa.streaming.domain.repositories;

import sk.posam.fsa.streaming.domain.models.entities.User;

import java.util.List;
import java.util.UUID;

public interface KeycloakRegistration {
    UUID register(User user);
    User update(User user);
    List<String> getUserRoles(UUID keycloakId);

}

