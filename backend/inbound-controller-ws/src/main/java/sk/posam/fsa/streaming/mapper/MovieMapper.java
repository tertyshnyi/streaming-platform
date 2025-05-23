package sk.posam.fsa.streaming.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.enums.Genre;
import sk.posam.fsa.streaming.domain.models.entities.Video;
import sk.posam.fsa.streaming.rest.dto.CreateMovieDto;
import sk.posam.fsa.streaming.rest.dto.GenreDto;
import sk.posam.fsa.streaming.rest.dto.MovieDto;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieMapper {

    private final VideoMapper videoMapper;

    public MovieMapper(VideoMapper videoMapper) {
        this.videoMapper = videoMapper;
    }

    public MovieDto toDto(Movie movie) {
        if (movie == null) return null;

        MovieDto dto = new MovieDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setSlug(movie.getSlug());
        dto.setDescription(movie.getDescription());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setDuration(movie.getDuration());

        dto.setGenres(
                movie.getGenres()
                        .stream()
                        .map(genre -> GenreDto.valueOf(genre.name()))
                        .collect(Collectors.toList())
        );

        dto.setActors(stringToList(movie.getActors()));
        dto.setDirectors(stringToList(movie.getDirectors()));
        dto.setTrailerUrl(movie.getTrailerUrl());
        dto.setCountries(movie.getCountries());
        dto.setPosterImg(movie.getPosterImg());
        dto.setCoverImg(movie.getCoverImg());
        dto.setCreatedAt(movie.getCreatedAt() != null ? movie.getCreatedAt().atOffset(ZoneOffset.UTC) : null);
        dto.setUpdatedAt(movie.getUpdatedAt() != null ? movie.getUpdatedAt().atOffset(ZoneOffset.UTC) : null);
        dto.setCommentsTotal(movie.getCommentsTotal());
        dto.setGlobalRating(movie.getGlobalRating() != null ? movie.getGlobalRating().floatValue() : null);

        if (movie.getVideos() != null) {
            dto.setVideos(movie.getVideos().stream()
                    .map(videoMapper::toDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }


    public Movie toEntity(CreateMovieDto createDto) {
        if (createDto == null) return null;

        Movie movie = new Movie();
        movie.setTitle(createDto.getTitle());
        movie.setDescription(createDto.getDescription());
        movie.setReleaseDate(createDto.getReleaseDate());
        movie.setReleaseYear(createDto.getReleaseYear());
        movie.setDuration(createDto.getDuration());
        movie.setGenres(
                createDto.getGenres()
                        .stream()
                        .map(genreDto -> Genre.valueOf(genreDto.name()))
                        .collect(Collectors.toList())
        );
        movie.setActors(listToString(createDto.getActors()));
        movie.setDirectors(listToString(createDto.getDirectors()));
        movie.setTrailerUrl(createDto.getTrailerUrl());
        movie.setCountries(createDto.getCountries());
        movie.setPosterImg(createDto.getPosterImg());
        movie.setCoverImg(createDto.getCoverImg());

        if (createDto.getVideos() != null) {
            List<Video> videos = createDto.getVideos().stream()
                    .map(videoDto -> videoMapper.toEntity(videoDto, null, movie))
                    .collect(Collectors.toList());
            movie.setVideos(videos);
        }

        return movie;
    }

    private String listToString(List<String> list) {
        return list == null ? null : String.join(", ", list);
    }

    private List<String> stringToList(String str) {
        if (str == null || str.isEmpty()) return List.of();
        return List.of(str.split("\\s*,\\s*"));
    }

    private List<String> toGenres(List<Genre> genres) {
        return genres != null ? genres.stream().map(Enum::name).toList() : List.of();
    }

    private List<Genre> toGenresEnum(List<String> genreStrings) {
        return genreStrings != null ? genreStrings.stream().map(Genre::valueOf).toList() : List.of();
    }
}
