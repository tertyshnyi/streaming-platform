package sk.posam.fsa.streaming.domain.repositories;

import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.MediaContentFilter;

import java.util.List;
import java.util.Optional;

public interface MediaContentRepository<T extends MediaContent> {
    Optional<T> get(Long id);
    T create(T entity);
    T update(Long id, T entity);
    void delete(Long id);
    List<T> findAll();
    List<T> filter(MediaContentFilter filter);
    List<T> searchByText(String text);
    List<T> findLatest(int count);
}
