package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.repositories.KeycloakRegistration;
import sk.posam.fsa.streaming.domain.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

public class UserService implements UserFacade {
    private final UserRepository userRepository;
    private final KeycloakRegistration keycloakRegistration;

    public UserService(UserRepository userRepository, KeycloakRegistration keycloakRegistration) {
        this.userRepository = userRepository;
        this.keycloakRegistration = keycloakRegistration;
    }

    @Override
    public User createWithKeycloak(User user) {
        UUID keycloakId = keycloakRegistration.register(user);
        user.setKeycloakId(keycloakId);

        List<String> roles = keycloakRegistration.getUserRoles(keycloakId);

        user.setAuthorities(String.join(",", roles));

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        return userRepository.create(user);
    }

    @Override
    public User updateWithKeycloak(User user) {
        try {
            keycloakRegistration.update(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user in Keycloak: " + e.getMessage(), e);
        }
        return userRepository.update(user);
    }

    @Override
    public User create(User user){
        try {
            if (user.getCreatedAt() == null) {
                user.setCreatedAt(LocalDateTime.now());
            }
            return userRepository.create(user);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create user: " + e.getMessage(), e);
        }
    }

    @Override
    public User update(User user) {
        try {
            return userRepository.update(user);
        } catch (Exception e) {
            throw new RuntimeException("Unable to update user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> get(Long id) {
        try {
            return userRepository.get(id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by id: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findByKeycloakId(UUID keycloakId) {
        try {
            return userRepository.findByKeycloakId(keycloakId);
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by keycloakId: " + e.getMessage(), e);
        }
    }
}
