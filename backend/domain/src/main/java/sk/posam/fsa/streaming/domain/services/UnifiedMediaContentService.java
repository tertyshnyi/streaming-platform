package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.Series;

import java.util.Optional;

public class UnifiedMediaContentService implements UnifiedMediaContentFacade {

    private final MovieFacade movieFacade;
    private final SeriesFacade seriesFacade;

    public UnifiedMediaContentService(MovieFacade movieFacade, SeriesFacade seriesFacade) {
        this.movieFacade = movieFacade;
        this.seriesFacade = seriesFacade;
    }

    @Override
    public Optional<MediaContent> get(Long id) {
        Optional<Movie> movieOpt = movieFacade.get(id);
        if (movieOpt.isPresent()) {
            return Optional.of(movieOpt.get());
        }

        Optional<Series> seriesOpt = seriesFacade.get(id);
        if (seriesOpt.isPresent()) {
            return Optional.of(seriesOpt.get());
        }

        return Optional.empty();
    }

    @Override
    public void incrementCommentsTotal(Long id) {
        if (movieFacade.get(id).isPresent()) {
            movieFacade.incrementCommentsTotal(id);
        } else if (seriesFacade.get(id).isPresent()) {
            seriesFacade.incrementCommentsTotal(id);
        }
    }

    @Override
    public void decrementCommentsTotal(Long id, int decrementBy) {
        if (movieFacade.get(id).isPresent()) {
            movieFacade.decrementCommentsTotal(id, decrementBy);
        } else if (seriesFacade.get(id).isPresent()) {
            seriesFacade.decrementCommentsTotal(id, decrementBy);
        }
    }
}
