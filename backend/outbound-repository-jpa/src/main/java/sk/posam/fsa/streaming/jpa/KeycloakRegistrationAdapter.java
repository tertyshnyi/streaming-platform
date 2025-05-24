package sk.posam.fsa.streaming.jpa;

import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.repositories.KeycloakRegistration;

@Component
public class KeycloakRegistrationAdapter implements KeycloakRegistration {
    private final KeycloakClient client;

    public KeycloakRegistrationAdapter(KeycloakClient client) {
        this.client = client;
    }

    @Override
    public String register(User user) {
        return client.registerUser(user);
    }

    @Override
    public User update(User user) {
        client.updateUser(user);
        return user;
    }
}
