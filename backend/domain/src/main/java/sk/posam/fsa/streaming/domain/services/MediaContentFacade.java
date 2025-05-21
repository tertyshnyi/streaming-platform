package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.enums.Genre;

import java.util.List;
import java.util.Optional;

public interface MediaContentFacade<T extends MediaContent> {
    Optional<T> get(Long id);
    T create(T mediaContent);
    void delete(Long id);
    List<T> findAll();
    List<T> findByGenre(Genre genre);
    List<T> findByReleaseYear(Integer year);
    List<T> findByCountry(String country);
    List<T> findTopByCommentsCount(int limit);
}
