package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.MediaContentFilter;
import sk.posam.fsa.streaming.domain.models.enums.Genre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<String> getAllCountries() {
        List<String> movieCountries = movieFacade.getDistinctCountries();
        List<String> seriesCountries = seriesFacade.getDistinctCountries();

        return Stream.concat(movieCountries.stream(), seriesCountries.stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Integer> getAllReleaseYears() {
        List<Integer> movieYears = movieFacade.getDistinctReleaseYears();
        List<Integer> seriesYears = seriesFacade.getDistinctReleaseYears();

        return Stream.concat(movieYears.stream(), seriesYears.stream())
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public List<MediaContent> findLatestMedia(int count) {
        List<MediaContent> movies = new ArrayList<>(movieFacade.findLatestMedia(count / 2 + 1));
        List<MediaContent> series = new ArrayList<>(seriesFacade.findLatestMedia(count / 2 + 1));

        return Stream.concat(movies.stream(), series.stream())
                .sorted(Comparator.comparing(MediaContent::getCreatedAt).reversed())
                .limit(count)
                .toList();
    }
}


