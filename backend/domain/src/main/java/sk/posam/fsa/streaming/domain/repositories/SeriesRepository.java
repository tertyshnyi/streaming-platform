package sk.posam.fsa.streaming.domain.repositories;

import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.Series;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends MediaContentRepository<Series>, SlugRepository<Series> {
    Series save(Series series);
    Optional<Series> findById(Long id);
    List<Integer> findDistinctReleaseYears();
    List<String> findDistinctCountries();
    @Override
    Optional<Series> findBySlug(String slug);
}

