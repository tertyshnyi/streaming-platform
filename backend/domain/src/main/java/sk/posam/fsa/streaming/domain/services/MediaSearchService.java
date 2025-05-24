package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.MediaContentFilter;

import java.util.ArrayList;
import java.util.List;

public class MediaSearchService {

    private final MovieFacade movieFacade;
    private final SeriesFacade seriesFacade;

    public MediaSearchService(MovieFacade movieFacade, SeriesFacade seriesFacade) {
        this.movieFacade = movieFacade;
        this.seriesFacade = seriesFacade;
    }

    public List<MediaContent> searchByText(String text) {
        List<MediaContent> result = new ArrayList<>();
        result.addAll(movieFacade.searchByText(text));
        result.addAll(seriesFacade.searchByText(text));
        return result;
    }

    public List<MediaContent> filter(MediaContentFilter filter) {
        List<MediaContent> result = new ArrayList<>();

        if (filter.getType() == null || filter.getType().equalsIgnoreCase("MOVIE")) {
            result.addAll(movieFacade.filter(filter));
        }

        if (filter.getType() == null || filter.getType().equalsIgnoreCase("SERIES")) {
            result.addAll(seriesFacade.filter(filter));
        }

        return result;
    }

}


