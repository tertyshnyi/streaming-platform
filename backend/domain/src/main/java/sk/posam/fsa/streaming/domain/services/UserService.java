package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.repositories.UserRepository;

import java.util.Optional;

public class UserService implements UserFacade {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by id: " + e.getMessage(), e);
        }
    }

    @Override
    public User create(User user){
        try {
            return userRepository.create(user);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create user: " + e.getMessage(), e);
        }
    }

    @Override
    public User update(User user){
        try {
            if (user.getId() == null || userRepository.findById(user.getId()).isEmpty()) {
                throw new RuntimeException("User not found with id: " + user.getId());
            }
            return userRepository.update(user);
        } catch (Exception e) {
            throw new RuntimeException("Unable to update user: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id){
        if (userRepository.findById(id).isPresent()) {
            throw new RuntimeException("User not find by id: " + id);
        }
        try {
            userRepository.delete(id);
        } catch (Exception e) {
           throw new RuntimeException("Unable to delete user with id: " + id + ". " + e.getMessage(), e);
        }
    }
}
