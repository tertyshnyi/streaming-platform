package sk.posam.fsa.streaming.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.streaming.domain.models.entities.*;
import sk.posam.fsa.streaming.domain.models.enums.Genre;
import sk.posam.fsa.streaming.domain.services.SeriesFacade;
import sk.posam.fsa.streaming.domain.services.UserFacade;
import sk.posam.fsa.streaming.mapper.EpisodeMapper;
import sk.posam.fsa.streaming.mapper.SeriesMapper;
import sk.posam.fsa.streaming.mapper.VideoMapper;
import sk.posam.fsa.streaming.rest.api.SeriesApi;
import sk.posam.fsa.streaming.rest.dto.*;
import sk.posam.fsa.streaming.security.CurrentUserDetailService;

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
    private final UserFacade userFacade;
    private final CurrentUserDetailService currentUserDetailService;

    public SeriesRestController(SeriesFacade seriesFacade, SeriesMapper seriesMapper, EpisodeMapper episodeMapper, VideoMapper videoMapper, UserFacade userFacade, CurrentUserDetailService currentUserDetailService) {
        this.seriesFacade = seriesFacade;
        this.seriesMapper = seriesMapper;
        this.episodeMapper = episodeMapper;
        this.videoMapper = videoMapper;
        this.userFacade = userFacade;
        this.currentUserDetailService = currentUserDetailService;
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
        UserDto userDto = currentUserDetailService.getCurrentUser();
        User user = userFacade.findByKeycloakId(userDto.getKeycloakId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Series series = seriesMapper.toEntity(createSeriesDto, user, user);
        Series saved = seriesFacade.create(series);
        return ResponseEntity.status(HttpStatus.CREATED).body(seriesMapper.toDto(saved));
    }

    @Override
    public ResponseEntity<SeriesDto> updateSeries(Long id, CreateSeriesDto dto) {
        try {
            UserDto userDto = currentUserDetailService.getCurrentUser();
            User user = userFacade.findByKeycloakId(userDto.getKeycloakId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Series existingSeries = seriesFacade.get(id)
                    .orElseThrow(() -> new NoSuchElementException("Series not found"));

            seriesMapper.updateEntity(existingSeries, dto, user);

            Series updated = seriesFacade.update(id, existingSeries);
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

            episodeMapper.updateEntity(episode, episodeDto);

            seriesFacade.updateEpisode(seriesId, episodeId, episode);

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
