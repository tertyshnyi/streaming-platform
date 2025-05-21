package sk.posam.fsa.streaming.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.posam.fsa.streaming.domain.models.entities.Series;

public interface SeriesSpringDataRepository extends JpaRepository<Series, Long> {
}
