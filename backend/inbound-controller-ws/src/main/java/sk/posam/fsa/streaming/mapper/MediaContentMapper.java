package sk.posam.fsa.streaming.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.Series;
import sk.posam.fsa.streaming.rest.dto.GenreDto;
import sk.posam.fsa.streaming.rest.dto.MediaContentDto;
import sk.posam.fsa.streaming.rest.dto.MediaContentTopDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MediaContentMapper {

    public MediaContentDto toDto(MediaContent content) {
        MediaContentDto dto = new MediaContentDto();

        dto.setId(content.getId());
        dto.setTitle(content.getTitle());
        dto.setDescription(content.getDescription());
        dto.setSlug(content.getSlug());
        dto.setGlobalRating(content.getGlobalRating());
        dto.setReleaseDate(content.getReleaseDate());
        dto.setPosterImg(content.getPosterImg());
        dto.setCountries(content.getCountries());

        if (content.getGenres() != null) {
            List<GenreDto> genreDtos = content.getGenres().stream()
                    .map(genre -> safeGenreDtoFromString(genre.name()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            dto.setGenres(genreDtos);
        }

        if (content instanceof Movie) {
            dto.setType("MOVIE");
        } else if (content instanceof Series) {
            dto.setType("SERIES");
        } else {
            dto.setType("UNKNOWN");
        }

        return dto;
    }

    public MediaContentTopDto toShortDto(MediaContent content) {
        MediaContentTopDto dto = new MediaContentTopDto();

        dto.setId(content.getId());
        dto.setTitle(content.getTitle());
        dto.setDescription(content.getDescription());
        dto.setSlug(content.getSlug());
        dto.setCoverImg(content.getCoverImg());

        if (content instanceof Movie) {
            dto.setType("MOVIE");
        } else if (content instanceof Series) {
            dto.setType("SERIES");
        } else {
            dto.setType("UNKNOWN");
        }

        return dto;
    }

    private GenreDto safeGenreDtoFromString(String name) {
        try {
            return GenreDto.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
