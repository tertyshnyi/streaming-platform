package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

public class UserService implements UserFacade {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public Optional<User> get(UUID id) {
        try {
            return userRepository.get(id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by id: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(UUID id) {
        Optional<User> user = userRepository.get(id);
        if (user.isEmpty()) {
            throw new NoSuchElementException("User not found by id: " + id);
        }

        try {
            userRepository.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Unable to delete user with id: " + id + ". " + e.getMessage(), e);
        }
    }
}
