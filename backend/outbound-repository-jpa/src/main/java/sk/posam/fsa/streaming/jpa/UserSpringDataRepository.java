package sk.posam.fsa.streaming.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.posam.fsa.streaming.domain.models.entities.User;

import java.util.UUID;

public interface UserSpringDataRepository extends JpaRepository<User, UUID> {
}
