package sk.posam.fsa.streaming.domain.repositories;

import sk.posam.fsa.streaming.domain.models.entities.Movie;

import java.util.Optional;

public interface MovieRepository extends MediaContentRepository<Movie>, SlugRepository<Movie> {
    @Override
    Optional<Movie> findBySlug(String slug);
}

