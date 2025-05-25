package sk.posam.fsa.streaming.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.Series;
import sk.posam.fsa.streaming.rest.dto.GenreDto;
import sk.posam.fsa.streaming.rest.dto.MediaContentDto;

import java.util.List;
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
                    .map(genre -> GenreDto.valueOf(genre.name()))
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
}
