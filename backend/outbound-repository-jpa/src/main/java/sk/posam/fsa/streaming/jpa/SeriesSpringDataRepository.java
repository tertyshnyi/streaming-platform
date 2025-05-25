package sk.posam.fsa.streaming.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.Series;

import java.util.Optional;

public interface SeriesSpringDataRepository extends JpaRepository<Series, Long> {
    Optional<Series> findBySlug(String slug);
}
