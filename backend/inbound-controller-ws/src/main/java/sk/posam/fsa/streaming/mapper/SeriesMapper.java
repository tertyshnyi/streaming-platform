package sk.posam.fsa.streaming.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.Episode;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.Series;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.models.enums.Genre;
import sk.posam.fsa.streaming.rest.dto.*;

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
        dto.setType(series.getType());
        dto.setSlug(series.getSlug());
        dto.setDescription(series.getDescription());
        dto.setReleaseDate(series.getReleaseDate());
        dto.setReleaseYear(series.getReleaseYear());
        dto.setGenres(toGenreDtoList(series.getGenres()));
        dto.setActors(stringToList(series.getActors()));
        dto.setDirectors(stringToList(series.getDirectors()));
        dto.setTrailerUrl(series.getTrailerUrl());
        dto.setCountries(series.getCountries());
        dto.setPosterImg(series.getPosterImg());
        dto.setCoverImg(series.getCoverImg());
        dto.setEpisodeCount(series.getEpisodeCount());
        dto.setAvgDuration(series.getAvgDuration());
        dto.setEpisodes(toMinimalEpisodeDtoList(series.getEpisodes()));
        dto.setCreatedAt(series.getCreatedAt() != null ? series.getCreatedAt().atOffset(ZoneOffset.UTC) : null);
        dto.setUpdatedAt(series.getUpdatedAt() != null ? series.getUpdatedAt().atOffset(ZoneOffset.UTC) : null);
        dto.setCreatedBy(series.getCreatedBy() != null ? series.getCreatedBy().getId().toString() : null);
        dto.setUpdatedBy(series.getUpdatedBy() != null ? series.getUpdatedBy().getId().toString() : null);
        dto.setCommentsTotal(series.getCommentsTotal());
        dto.setGlobalRating(series.getGlobalRating());

        return dto;
    }

    public Series toEntity(CreateSeriesDto dto, User createdBy, User updatedBy) {
        if (dto == null) return null;

        Series series = new Series();
        series.setTitle(dto.getTitle());
        series.setType(dto.getType());
        series.setDescription(dto.getDescription());
        series.setReleaseDate(dto.getReleaseDate());
        series.setReleaseYear(dto.getReleaseYear());
        series.setGenres(dto.getGenres().stream()
                .map(g -> Genre.valueOf(g.name()))
                .collect(Collectors.toList()));
        series.setActors(listToString(dto.getActors()));
        series.setDirectors(listToString(dto.getDirectors()));
        series.setTrailerUrl(dto.getTrailerUrl());
        series.setCountries(dto.getCountries());
        series.setPosterImg(dto.getPosterImg());
        series.setCoverImg(dto.getCoverImg());
        series.setEpisodeCount(dto.getEpisodeCount());
        series.setAvgDuration(dto.getAvgDuration());
        series.setCreatedBy(createdBy);
        series.setGlobalRating(dto.getGlobalRating());
        series.setUpdatedBy(null);

        if (dto.getEpisodes() != null) {
            List<Episode> episodes = dto.getEpisodes().stream()
                    .map(episodeDto -> episodeMapper.toEntity(episodeDto, series))
                    .collect(Collectors.toList());
            series.setEpisodes(episodes);
        }

        return series;
    }

    public void updateEntity(Series existingSeries, CreateSeriesDto dto, User updatedBy) {
        if (dto == null || existingSeries == null) return;

        existingSeries.setTitle(dto.getTitle());
        existingSeries.setType(dto.getType());
        existingSeries.setDescription(dto.getDescription());
        existingSeries.setReleaseDate(dto.getReleaseDate());
        existingSeries.setReleaseYear(dto.getReleaseYear());
        existingSeries.setGenres(dto.getGenres().stream()
                .map(g -> Genre.valueOf(g.name()))
                .collect(Collectors.toList()));
        existingSeries.setActors(listToString(dto.getActors()));
        existingSeries.setDirectors(listToString(dto.getDirectors()));
        existingSeries.setTrailerUrl(dto.getTrailerUrl());
        existingSeries.setCountries(dto.getCountries());
        existingSeries.setPosterImg(dto.getPosterImg());
        existingSeries.setCoverImg(dto.getCoverImg());
        existingSeries.setEpisodeCount(dto.getEpisodeCount());
        existingSeries.setAvgDuration(dto.getAvgDuration());
        existingSeries.setUpdatedBy(updatedBy);
        existingSeries.setGlobalRating(dto.getGlobalRating());

        if (dto.getEpisodes() != null) {
            List<Episode> newEpisodes = dto.getEpisodes().stream()
                    .map(episodeDto -> episodeMapper.toEntity(episodeDto, existingSeries))
                    .collect(Collectors.toList());

            List<Episode> existingEpisodes = existingSeries.getEpisodes();

            existingEpisodes.removeIf(existing -> newEpisodes.stream()
                    .noneMatch(ne -> ne.getId() != null && ne.getId().equals(existing.getId()))
            );

            for (Episode newEp : newEpisodes) {
                if (newEp.getId() == null) {
                    existingEpisodes.add(newEp);
                } else {
                    existingEpisodes.stream()
                            .filter(ex -> ex.getId().equals(newEp.getId()))
                            .findFirst()
                            .ifPresent(existingEp -> {
                                existingEp.setTitle(newEp.getTitle());
                                existingEp.setDuration(newEp.getDuration());
                                existingEp.setVideos(newEp.getVideos());
                            });
                }
            }
        }
    }

    public SeriesDto toMinimalDto(Series series) {
        if (series == null) return null;

        SeriesDto dto = new SeriesDto();
        dto.setId(series.getId());
        dto.setTitle(series.getTitle());
        return dto;
    }

    private String listToString(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return String.join(", ", list);
    }

    private List<String> stringToList(String str) {
        if (str == null || str.isEmpty()) return List.of();
        return List.of(str.split("\\s*,\\s*"));
    }

    private List<GenreDto> toGenreDtoList(List<Genre> genres) {
        if (genres == null) return null;

        return genres.stream()
                .map(genre -> GenreDto.valueOf(genre.name()))
                .collect(Collectors.toList());
    }

    private List<EpisodeDto> toMinimalEpisodeDtoList(List<Episode> episodes) {
        if (episodes == null) return null;

        return episodes.stream().map(episode -> {
            EpisodeDto dto = new EpisodeDto();
            dto.setId(episode.getId());
            dto.setTitle(episode.getTitle());
            dto.setDuration(episode.getDuration());
            dto.setVideos(null);
            dto.setSeriesId(episode.getSeries() != null ? episode.getSeries().getId().intValue() : null);
            return dto;
        }).collect(Collectors.toList());
    }
}
