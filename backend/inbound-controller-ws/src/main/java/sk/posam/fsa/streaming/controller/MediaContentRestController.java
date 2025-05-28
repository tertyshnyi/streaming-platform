package sk.posam.fsa.streaming.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.MediaContentFilter;
import sk.posam.fsa.streaming.domain.models.enums.Genre;
import sk.posam.fsa.streaming.domain.services.MediaSearchService;
import sk.posam.fsa.streaming.mapper.MediaContentMapper;
import sk.posam.fsa.streaming.rest.api.MediaApi;
import sk.posam.fsa.streaming.rest.dto.GenreDto;
import sk.posam.fsa.streaming.rest.dto.MediaContentDto;
import sk.posam.fsa.streaming.rest.dto.MediaContentTopDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MediaContentRestController implements MediaApi {

    private final MediaSearchService mediaSearchService;
    private final MediaContentMapper mediaContentMapper;

    public MediaContentRestController(MediaSearchService mediaSearchService, MediaContentMapper mediaContentMapper) {
        this.mediaSearchService = mediaSearchService;
        this.mediaContentMapper = mediaContentMapper;
    }

    @Override
    public ResponseEntity<List<MediaContentDto>> mediaSearchGet(String query) {
        List<MediaContent> results = mediaSearchService.searchByText(query);
        List<MediaContentDto> dtos = results.stream()
                .map(mediaContentMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<List<MediaContentDto>> mediaFilterGet(
            String type,
            List<GenreDto> genresDto,
            List<String> countries,
            List<Integer> releaseYears,
            Float ratingFrom,
            Float ratingTo) {

        List<Genre> genres = null;
        if (genresDto != null) {
            genres = genresDto.stream()
                    .map(g -> Genre.valueOf(g.name()))
                    .collect(Collectors.toList());
        }

        MediaContentFilter filter = new MediaContentFilter();
        filter.setType(type);
        filter.setGenres(genres);
        filter.setCountries(countries);
        filter.setReleaseYears(releaseYears);
        filter.setRatingFrom(ratingFrom);
        filter.setRatingTo(ratingTo);

        List<MediaContent> filtered = mediaSearchService.filter(filter);
        List<MediaContentDto> dtos = filtered.stream()
                .map(mediaContentMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<List<String>> getGenres() {
        List<String> genres = Arrays.stream(Genre.values())
                .map(Genre::toString)
                .collect(Collectors.toList());
        return ResponseEntity.ok(genres);
    }

    @Override
    public ResponseEntity<List<MediaContentTopDto>> getLatestMedia() {
        List<MediaContent> latest = mediaSearchService.findLatestMedia(5);
        List<MediaContentTopDto> result = latest.stream()
                .map(mediaContentMapper::toShortDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<String>> getCountries() {
        List<String> countries = mediaSearchService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @Override
    public ResponseEntity<List<Integer>> getReleaseYears() {
        List<Integer> years = mediaSearchService.getAllReleaseYears();
        return ResponseEntity.ok(years);
    }
}

