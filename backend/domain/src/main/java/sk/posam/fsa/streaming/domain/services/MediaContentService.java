package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.MediaContentFilter;
import sk.posam.fsa.streaming.domain.models.enums.Genre;
import sk.posam.fsa.streaming.domain.repositories.MediaContentRepository;
import sk.posam.fsa.streaming.domain.repositories.SlugRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class MediaContentService<T extends MediaContent> implements MediaContentFacade<T> {

    protected final MediaContentRepository<T> repository;
    protected final SlugRepository<T> slugRepository;

    public MediaContentService(MediaContentRepository<T> repository, SlugRepository<T> slugRepository) {
        this.repository = repository;
        this.slugRepository = slugRepository;
    }

    @Override
    public List<T> searchByText(String text) {
        return repository.searchByText(text);
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
        if (mediaContent.getCreatedAt() == null) {
            mediaContent.setCreatedAt(LocalDateTime.now());
        }
        mediaContent.setUpdatedAt(LocalDateTime.now());

        String baseSlug = mediaContent.generateBaseSlug();
        String uniqueSlug = generateUniqueSlug(baseSlug);
        mediaContent.setSlug(uniqueSlug);

        mediaContent.updateCommentsTotal();

        return repository.create(mediaContent);
    }

    @Override
    public T update(Long id, T mediaContent) {
        T existing = repository.get(id).orElseThrow(() ->
                new NoSuchElementException("Media content not found with id: " + id));

        mediaContent.setId(id);
        mediaContent.setCreatedAt(existing.getCreatedAt());
        mediaContent.setUpdatedAt(LocalDateTime.now());
        mediaContent.setSlug(existing.getSlug());

        return repository.update(id, mediaContent);
    }

    private String generateUniqueSlug(String baseSlug) {
        String slug = baseSlug;
        int suffix = 1;

        while (slugRepository.findBySlug(slug).isPresent()) {
            slug = baseSlug + "-" + suffix;
            suffix++;
        }

        return slug;
    }

    @Override
    public List<T> filter(MediaContentFilter filter) {
        return repository.filter(filter);
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
}

