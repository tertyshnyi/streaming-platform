package sk.posam.fsa.streaming.domain.repositories;

import java.util.Optional;

public interface SlugRepository<T> {
    Optional<T> findBySlug(String slug);
}
