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
        series.setActors(listToString(dto.getActors()));
        series.setDirectors(listToString(dto.getDirectors()));
        series.setTrailerUrl(dto.getTrailerUrl());
        series.setCountries(dto.getCountries());
        series.setPosterImg(dto.getPosterImg());
        series.setCoverImg(dto.getCoverImg());
        series.setEpisodeCount(dto.getEpisodeCount());
        series.setAvgDuration(dto.getAvgDuration());
        if (dto.getEpisodes() != null) {
            List<Episode> episodes = dto.getEpisodes().stream()
                    .map(episodeDto -> episodeMapper.toEntity(episodeDto, series))
                    .collect(Collectors.toList());
            series.setEpisodes(episodes);
        }

        return series;
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
