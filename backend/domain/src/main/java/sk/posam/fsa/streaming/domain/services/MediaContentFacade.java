package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.MediaContentFilter;
import sk.posam.fsa.streaming.domain.models.enums.Genre;

import java.util.List;
import java.util.Optional;

public interface MediaContentFacade<T extends MediaContent> {
    Optional<T> get(Long id);
    T create(T mediaContent);
    T update(Long id, T mediaContent);
    void delete(Long id);
    List<T> findAll();
    List<T> filter(MediaContentFilter filter);
    List<T> searchByText(String text);
}
