package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.models.enums.Authority;
import sk.posam.fsa.streaming.domain.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

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
            if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
                Set<Authority> authorities = new HashSet<>();
                authorities.add(Authority.USER);
                user.setAuthorities(authorities);
            }
            return userRepository.create(user);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> get(Long requestedId, Long currentUserId) {
        if (!requestedId.equals(currentUserId)) {
            throw new SecurityException("Access denied");
        }

        try {
            return userRepository.get(requestedId);
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by id: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long requestedId, Long currentUserId) {
        if (!requestedId.equals(currentUserId)) {
            throw new SecurityException("Access denied");
        }

        Optional<User> user = userRepository.get(requestedId);
        if (user.isEmpty()) {
            throw new NoSuchElementException("User not found by id: " + requestedId);
        }

        try {
            userRepository.delete(requestedId);
        } catch (Exception e) {
            throw new RuntimeException("Unable to delete user with id: " + requestedId + ". " + e.getMessage(), e);
        }
    }

    @Override
    public String login(String emailOrPhone, String password) {
        User user = userRepository.findByEmailOrPhoneNumber(emailOrPhone)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return "Login successful for user: " + user.getName();
    }
}
