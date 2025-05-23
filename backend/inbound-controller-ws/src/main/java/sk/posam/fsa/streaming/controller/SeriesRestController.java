package sk.posam.fsa.streaming.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.streaming.domain.models.entities.*;
import sk.posam.fsa.streaming.domain.models.enums.Genre;
import sk.posam.fsa.streaming.domain.services.SeriesFacade;
import sk.posam.fsa.streaming.mapper.EpisodeMapper;
import sk.posam.fsa.streaming.mapper.SeriesMapper;
import sk.posam.fsa.streaming.mapper.VideoMapper;
import sk.posam.fsa.streaming.rest.api.SeriesApi;
import sk.posam.fsa.streaming.rest.dto.*;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class SeriesRestController implements SeriesApi {

    private final SeriesFacade seriesFacade;
    private final SeriesMapper seriesMapper;
    private final EpisodeMapper episodeMapper;
    private final VideoMapper videoMapper;

    public SeriesRestController(SeriesFacade seriesFacade, SeriesMapper seriesMapper, EpisodeMapper episodeMapper, VideoMapper videoMapper) {
        this.seriesFacade = seriesFacade;
        this.seriesMapper = seriesMapper;
        this.episodeMapper = episodeMapper;
        this.videoMapper = videoMapper;
    }

    @Override
    public ResponseEntity<List<SeriesDto>> seriesFilterGet(
            List<GenreDto> genresDto,
            List<String> countries,
            List<Integer> releaseYears,
            Float ratingFrom,
            Float ratingTo) {

        List<Genre> genres = null;
        if (genresDto != null) {
            genres = genresDto.stream()
                    .map(genreDto -> Genre.valueOf(genreDto.name()))
                    .collect(Collectors.toList());
        }

        MediaContentFilter filter = new MediaContentFilter();
        filter.setGenres(genres);
        filter.setCountries(countries);
        filter.setReleaseYears(releaseYears);
        filter.setRatingFrom(ratingFrom);
        filter.setRatingTo(ratingTo);

        List<Series> filteredSeries = seriesFacade.filter(filter);
        List<SeriesDto> seriesDtos = filteredSeries.stream()
                .map(seriesMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(seriesDtos);
    }

    @Override
    public ResponseEntity<Void> deleteSeries(Long id) {
        try {
            seriesFacade.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @Override
    public ResponseEntity<List<SeriesDto>> getAllSeries() {
        List<SeriesDto> seriesList = seriesFacade.findAll().stream()
                .map(seriesMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(seriesList);
    }

    @Override
    public ResponseEntity<SeriesDto> getSeriesById(Long id) {
        return seriesFacade.get(id)
                .map(seriesMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<SeriesDto> createSeries(CreateSeriesDto createSeriesDto) {
        Series series = seriesMapper.toEntity(createSeriesDto);
        Series saved = seriesFacade.create(series);
        return ResponseEntity.status(HttpStatus.CREATED).body(seriesMapper.toDto(saved));
    }

    @Override
    public ResponseEntity<SeriesDto> updateSeries(Long id, CreateSeriesDto dto) {
        try {
            Series series = seriesMapper.toEntity(dto);
            Series updated = seriesFacade.update(id, series);
            return ResponseEntity.ok(seriesMapper.toDto(updated));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<List<EpisodeDto>> getAllEpisodesOfSeries(Long seriesId) {
        try {
            List<Episode> episodes = seriesFacade.getAllEpisodes(seriesId);
            List<EpisodeDto> dtos = episodes.stream()
                    .map(episodeMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<EpisodeDto> getEpisodeById(Long seriesId, Long episodeId) {
        return seriesFacade.getEpisodeById(seriesId, episodeId)
                .map(episodeMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<EpisodeDto> addEpisodeToSeries(Long seriesId, EpisodeDto episodeDto) {
        try {
            Series series = seriesFacade.get(seriesId).orElseThrow(() -> new NoSuchElementException("Series not found"));
            Episode episode = episodeMapper.toEntity(episodeDto, series);
            Series updatedSeries = seriesFacade.addEpisode(seriesId, episode);
            Episode addedEpisode = updatedSeries.getEpisodes().stream()
                    .max(Comparator.comparing(Episode::getId))
                    .orElseThrow();
            return ResponseEntity.status(HttpStatus.CREATED).body(episodeMapper.toDto(addedEpisode));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<EpisodeDto> updateEpisodeById(Long seriesId, Long episodeId, EpisodeDto episodeDto) {
        try {
            Series series = seriesFacade.get(seriesId).orElseThrow(() -> new NoSuchElementException("Series not found"));
            Episode episode = seriesFacade.getEpisodeById(seriesId, episodeId).orElseThrow(() -> new NoSuchElementException("Episode not found"));

            episode.setTitle(episodeDto.getTitle());
            episode.setDuration(episodeDto.getDuration());

            if (episodeDto.getVideos() != null) {
                List<Video> updatedVideos = episodeDto.getVideos().stream()
                        .map(videoDto -> videoMapper.toEntity(videoDto, episode, null))
                        .toList();

                List<Video> currentVideos = episode.getVideos();

                currentVideos.removeIf(video -> !updatedVideos.contains(video));

                for (Video video : updatedVideos) {
                    if (!currentVideos.contains(video)) {
                        video.setEpisode(episode);
                        currentVideos.add(video);
                    }
                }
            }

            Series updatedSeries = seriesFacade.updateEpisode(seriesId, episodeId, episode);

            Episode updatedEpisode = seriesFacade.getEpisodeById(seriesId, episodeId)
                    .orElseThrow(() -> new NoSuchElementException("Episode not found after update"));

            return ResponseEntity.ok(episodeMapper.toDto(updatedEpisode));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteEpisodeById(Long seriesId, Long episodeId) {
        try {
            seriesFacade.removeEpisode(seriesId, episodeId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
