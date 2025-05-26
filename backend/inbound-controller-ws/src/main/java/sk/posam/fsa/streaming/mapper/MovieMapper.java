package sk.posam.fsa.streaming.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.User;
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
        dto.setType(movie.getType());
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
        dto.setCreatedBy(movie.getCreatedBy() != null ? movie.getCreatedBy().getId().toString() : null);
        dto.setUpdatedBy(movie.getUpdatedBy() != null ? movie.getUpdatedBy().getId().toString() : null);
        dto.setCommentsTotal(movie.getCommentsTotal());
        dto.setGlobalRating(movie.getGlobalRating());

        if (movie.getVideos() != null) {
            dto.setVideos(movie.getVideos().stream()
                    .map(videoMapper::toDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public Movie toEntity(CreateMovieDto createDto, User createdBy, User updatedBy) {
        if (createDto == null) return null;

        Movie movie = new Movie();
        movie.setTitle(createDto.getTitle());
        movie.setType(createDto.getType());
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
        movie.setGlobalRating(createDto.getGlobalRating());
        movie.setCreatedBy(createdBy);
        movie.setUpdatedBy(null);

        if (createDto.getVideos() != null) {
            List<Video> videos = createDto.getVideos().stream()
                    .map(videoDto -> videoMapper.toEntity(videoDto, null, movie))
                    .collect(Collectors.toList());
            movie.setVideos(videos);
        }

        return movie;
    }

    public void updateEntity(Movie existingMovie, CreateMovieDto dto, User updatedBy) {
        if (dto == null || existingMovie == null) return;

        existingMovie.setTitle(dto.getTitle());
        existingMovie.setType(dto.getType());
        existingMovie.setDescription(dto.getDescription());
        existingMovie.setReleaseDate(dto.getReleaseDate());
        existingMovie.setReleaseYear(dto.getReleaseYear());
        existingMovie.setDuration(dto.getDuration());
        existingMovie.setGenres(
                dto.getGenres()
                        .stream()
                        .map(genreDto -> Genre.valueOf(genreDto.name()))
                        .collect(Collectors.toList())
        );
        existingMovie.setActors(listToString(dto.getActors()));
        existingMovie.setDirectors(listToString(dto.getDirectors()));
        existingMovie.setTrailerUrl(dto.getTrailerUrl());
        existingMovie.setCountries(dto.getCountries());
        existingMovie.setPosterImg(dto.getPosterImg());
        existingMovie.setCoverImg(dto.getCoverImg());
        existingMovie.setUpdatedBy(updatedBy);
        existingMovie.setGlobalRating(dto.getGlobalRating());

        if (dto.getVideos() != null) {
            List<Video> newVideos = dto.getVideos().stream()
                    .map(videoDto -> videoMapper.toEntity(videoDto, null, existingMovie))
                    .collect(Collectors.toList());

            List<Video> existingVideos = existingMovie.getVideos();

            existingVideos.removeIf(ev -> newVideos.stream().noneMatch(nv -> nv.getId() != null && nv.getId().equals(ev.getId())));

            for (Video newVideo : newVideos) {
                if (newVideo.getId() == null) {
                    existingVideos.add(newVideo);
                } else {
                    existingVideos.stream()
                            .filter(ev -> ev.getId().equals(newVideo.getId()))
                            .findFirst()
                            .ifPresent(ev -> {
                                ev.setUrl(newVideo.getUrl());
                                ev.setResolution(newVideo.getResolution());
                            });
                }
            }
        }
    }

    private String listToString(List<String> list) {
        return list == null ? null : String.join(", ", list);
    }

    private List<String> stringToList(String str) {
        if (str == null || str.isEmpty()) return List.of();
        return List.of(str.split("\\s*,\\s*"));
    }
}
