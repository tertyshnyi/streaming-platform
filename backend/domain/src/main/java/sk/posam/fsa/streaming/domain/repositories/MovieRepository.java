package sk.posam.fsa.streaming.domain.repositories;

import sk.posam.fsa.streaming.domain.models.entities.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends MediaContentRepository<Movie>, SlugRepository<Movie> {
    @Override
    Optional<Movie> findBySlug(String slug);

    List<Integer> findDistinctReleaseYears();
    List<String> findDistinctCountries();
}

