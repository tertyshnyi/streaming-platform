package sk.posam.fsa.streaming.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.repositories.KeycloakRegistration;
import sk.posam.fsa.streaming.domain.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private KeycloakRegistration keycloakRegistration;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        keycloakRegistration = mock(KeycloakRegistration.class);
        userService = new UserService(userRepository, keycloakRegistration);
    }

    @Test
    void createWithKeycloak_shouldRegisterAndCreateUser() {
        User user = new User();
        user.setCreatedAt(null);

        UUID keycloakId = UUID.randomUUID();
        when(keycloakRegistration.register(user)).thenReturn(keycloakId);
        when(keycloakRegistration.getUserRoles(keycloakId)).thenReturn(List.of("ROLE_USER", "ROLE_ADMIN"));

        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setKeycloakId(keycloakId);
        createdUser.setAuthorities("ROLE_USER,ROLE_ADMIN");
        createdUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.create(any(User.class))).thenReturn(createdUser);

        User result = userService.createWithKeycloak(user);

        assertNotNull(result);
        assertEquals(keycloakId, result.getKeycloakId());
        assertEquals("ROLE_USER,ROLE_ADMIN", result.getAuthorities());
        assertNotNull(result.getCreatedAt());

        verify(keycloakRegistration).register(user);
        verify(keycloakRegistration).getUserRoles(keycloakId);
        verify(userRepository).create(any(User.class));
    }

    @Test
    void updateWithKeycloak_shouldUpdateKeycloakAndUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Original Name");

        User updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setName("Updated Name");

        when(keycloakRegistration.update(any(User.class))).thenReturn(updatedUser);
        when(userRepository.update(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateWithKeycloak(user);

        assertEquals(updatedUser, result);
        verify(keycloakRegistration).update(user);
        verify(userRepository).update(user);
    }

    @Test
    void create_shouldSetCreatedAtIfNullAndCreateUser() {
        User user = new User();
        user.setCreatedAt(null);

        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.create(any(User.class))).thenReturn(createdUser);

        User result = userService.create(user);

        assertNotNull(result);
        assertNotNull(result.getCreatedAt());
        verify(userRepository).create(any(User.class));
    }

    @Test
    void update_shouldUpdateUser() {
        User user = new User();
        user.setId(1L);

        when(userRepository.update(user)).thenReturn(user);

        User result = userService.update(user);

        assertEquals(user, result);
        verify(userRepository).update(user);
    }

    @Test
    void get_shouldReturnUserIfExists() {
        User user = new User();
        user.setId(1L);

        when(userRepository.get(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.get(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository).get(1L);
    }

    @Test
    void findByKeycloakId_shouldReturnUserIfExists() {
        UUID keycloakId = UUID.randomUUID();
        User user = new User();
        user.setKeycloakId(keycloakId);

        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByKeycloakId(keycloakId);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository).findByKeycloakId(keycloakId);
    }
}
