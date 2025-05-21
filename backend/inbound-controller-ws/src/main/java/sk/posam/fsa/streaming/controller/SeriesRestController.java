package sk.posam.fsa.streaming.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.streaming.domain.models.entities.Series;
import sk.posam.fsa.streaming.domain.services.SeriesFacade;
import sk.posam.fsa.streaming.mapper.EpisodeMapper;
import sk.posam.fsa.streaming.mapper.SeriesMapper;
import sk.posam.fsa.streaming.rest.api.SeriesApi;
import sk.posam.fsa.streaming.rest.dto.CreateSeriesDto;
import sk.posam.fsa.streaming.rest.dto.SeriesDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
public class SeriesRestController implements SeriesApi {

    private final SeriesFacade seriesFacade;
    private final SeriesMapper seriesMapper;
    private final EpisodeMapper episodeMapper;

    public SeriesRestController(SeriesFacade seriesFacade, SeriesMapper seriesMapper, EpisodeMapper episodeMapper) {
        this.seriesFacade = seriesFacade;
        this.seriesMapper = seriesMapper;
        this.episodeMapper = episodeMapper;
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

}
