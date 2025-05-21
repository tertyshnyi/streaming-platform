package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.enums.Genre;
import sk.posam.fsa.streaming.domain.repositories.MediaContentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class MediaContentService<T extends MediaContent> implements MediaContentFacade<T> {

    protected final MediaContentRepository<T> repository;

    public MediaContentService(MediaContentRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<T> get(Long id) {
        try {
            return repository.get(id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding media content by id: " + e.getMessage(), e);
        }
    }

    @Override
    public T create(T mediaContent) {
        try {
            if (mediaContent.getCreatedAt() == null) {
                mediaContent.setCreatedAt(LocalDateTime.now());
            }
            mediaContent.setUpdatedAt(LocalDateTime.now());
            return repository.create(mediaContent);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create media content: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        Optional<T> entity = repository.get(id);
        if (entity.isEmpty()) {
            throw new NoSuchElementException("Media content not found by id: " + id);
        }

        try {
            repository.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Unable to delete media content with id: " + id + ". " + e.getMessage(), e);
        }
    }

    @Override
    public List<T> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all media contents: " + e.getMessage(), e);
        }
    }

    @Override
    public List<T> findByGenre(Genre genre) {
        try {
            return repository.findByGenre(genre);
        } catch (Exception e) {
            throw new RuntimeException("Error finding media contents by genre: " + e.getMessage(), e);
        }
    }

    @Override
    public List<T> findByReleaseYear(Integer year) {
        try {
            return repository.findByReleaseYear(year);
        } catch (Exception e) {
            throw new RuntimeException("Error finding media contents by release year: " + e.getMessage(), e);
        }
    }

    @Override
    public List<T> findByCountry(String country) {
        try {
            return repository.findByCountry(country);
        } catch (Exception e) {
            throw new RuntimeException("Error finding media contents by country: " + e.getMessage(), e);
        }
    }

    @Override
    public List<T> findTopByCommentsCount(int limit) {
        try {
            return repository.findTopByCommentsCount(limit);
        } catch (Exception e) {
            throw new RuntimeException("Error finding top media contents by comments count: " + e.getMessage(), e);
        }
    }

}

