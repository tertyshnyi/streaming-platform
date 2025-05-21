package sk.posam.fsa.streaming.domain.repositories;

import sk.posam.fsa.streaming.domain.models.enums.Genre;
import sk.posam.fsa.streaming.domain.models.entities.MediaContent;

import java.util.List;
import java.util.Optional;

public interface MediaContentRepository<T extends MediaContent> {
    Optional<T> get(Long id);
    T create(T entity);
    void delete(Long id);
    List<T> findAll();
    List<T> findByGenre(Genre genre);

    List<T> findByReleaseYear(Integer year);

    List<T> findByCountry(String country);

    List<T> findTopByCommentsCount(int limit);
}
