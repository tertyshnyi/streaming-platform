package sk.posam.fsa.streaming.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.posam.fsa.streaming.domain.models.entities.Movie;

import java.util.Optional;

public interface MovieSpringDataRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findBySlug(String slug);
}

