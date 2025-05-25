package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.MediaContent;

import java.util.Optional;

public interface UnifiedMediaContentFacade {
    Optional<MediaContent> get(Long id);

    void incrementCommentsTotal(Long id);
    void decrementCommentsTotal(Long id);
}