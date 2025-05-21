package sk.posam.fsa.streaming.domain.repositories;

import sk.posam.fsa.streaming.domain.models.entities.Series;

import java.util.Optional;

public interface SeriesRepository extends MediaContentRepository<Series> {
    Series save(Series series);
    Optional<Series> findById(Long id);
}

