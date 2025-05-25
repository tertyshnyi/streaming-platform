package sk.posam.fsa.streaming.jpa;

import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.repositories.KeycloakRegistration;

import java.util.List;
import java.util.UUID;

@Component
public class KeycloakRegistrationAdapter implements KeycloakRegistration {
    private final KeycloakClient client;

    public KeycloakRegistrationAdapter(KeycloakClient client) {
        this.client = client;
    }

    @Override
    public UUID register(User user) {
        return UUID.fromString(client.registerUser(user));
    }

    @Override
    public User update(User user) {
        client.updateUser(user);
        return user;
    }

    @Override
    public List<String> getUserRoles(UUID keycloakId) {
        return client.getUserRoles(keycloakId.toString());
    }
}
