package sk.posam.fsa.streaming.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.posam.fsa.streaming.domain.models.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserSpringDataRepository extends JpaRepository<User, Long> {
    Optional<User> findByKeycloakId(UUID keycloakId);
}
