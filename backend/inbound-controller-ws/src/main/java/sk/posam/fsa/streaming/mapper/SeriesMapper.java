package sk.posam.fsa.streaming.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.Episode;
import sk.posam.fsa.streaming.domain.models.entities.Series;
import sk.posam.fsa.streaming.domain.models.enums.Genre;
import sk.posam.fsa.streaming.rest.dto.CreateSeriesDto;
import sk.posam.fsa.streaming.rest.dto.EpisodeDto;
import sk.posam.fsa.streaming.rest.dto.GenreDto;
import sk.posam.fsa.streaming.rest.dto.SeriesDto;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeriesMapper {

    private final EpisodeMapper episodeMapper;

    public SeriesMapper(EpisodeMapper episodeMapper) {
        this.episodeMapper = episodeMapper;
    }

    public SeriesDto toDto(Series series) {
        if (series == null) return null;

        SeriesDto dto = new SeriesDto();
        dto.setId(series.getId());
        dto.setTitle(series.getTitle());
        dto.setDescription(series.getDescription());
        dto.setReleaseDate(series.getReleaseDate());
        dto.setReleaseYear(series.getReleaseYear());
        dto.setGenres(toGenreDtoList(series.getGenres()));
        dto.setActors(series.getActors());
        dto.setDirectors(series.getDirectors());
        dto.setTrailerUrl(series.getTrailerUrl());
        dto.setCountries(series.getCountries());
        dto.setPosterImg(series.getPosterImg());
        dto.setCoverImg(series.getCoverImg());
        dto.setEpisodeCount(series.getEpisodeCount());
        dto.setAvgDuration(series.getAvgDuration());
        dto.setEpisodes(toEpisodeDtoList(series.getEpisodes(), series));
        dto.setCreatedAt(series.getCreatedAt() != null ? series.getCreatedAt().atOffset(ZoneOffset.UTC) : null);
        dto.setUpdatedAt(series.getUpdatedAt() != null ? series.getUpdatedAt().atOffset(ZoneOffset.UTC) : null);
        dto.setCommentsTotal(series.getCommentsTotal());
        dto.setGlobalRating(series.getGlobalRating() != null ? series.getGlobalRating().floatValue() : null);

        return dto;
    }

    public Series toEntity(CreateSeriesDto dto) {
        if (dto == null) return null;

        Series series = new Series();
        series.setTitle(dto.getTitle());
        series.setDescription(dto.getDescription());
        series.setReleaseDate(dto.getReleaseDate());
        series.setReleaseYear(dto.getReleaseYear());
        series.setGenres(dto.getGenres().stream()
                .map(g -> Genre.valueOf(g.name()))
                .collect(Collectors.toList()));
        series.setActors(dto.getActors());
        series.setDirectors(dto.getDirectors());
        series.setTrailerUrl(dto.getTrailerUrl());
        series.setCountries(dto.getCountries());
        series.setPosterImg(dto.getPosterImg());
        series.setCoverImg(dto.getCoverImg());
        series.setEpisodeCount(dto.getEpisodeCount());
        series.setAvgDuration(dto.getAvgDuration());

        return series;
    }

    public SeriesDto toMinimalDto(Series series) {
        if (series == null) return null;

        SeriesDto dto = new SeriesDto();
        dto.setId(series.getId());
        dto.setTitle(series.getTitle());
        return dto;
    }

    private List<GenreDto> toGenreDtoList(List<Genre> genres) {
        if (genres == null) return null;

        return genres.stream()
                .map(genre -> GenreDto.valueOf(genre.name()))
                .collect(Collectors.toList());
    }

    private List<EpisodeDto> toEpisodeDtoList(List<Episode> episodes, Series series) {
        if (episodes == null) return null;

        return episodes.stream()
                .map(episodeMapper::toDto)
                .collect(Collectors.toList());
    }
}
