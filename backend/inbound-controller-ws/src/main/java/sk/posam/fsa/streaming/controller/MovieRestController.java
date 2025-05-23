package sk.posam.fsa.streaming.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.streaming.domain.models.entities.MediaContentFilter;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.Video;
import sk.posam.fsa.streaming.domain.models.enums.Genre;
import sk.posam.fsa.streaming.domain.services.MovieFacade;
import sk.posam.fsa.streaming.mapper.MovieMapper;
import sk.posam.fsa.streaming.mapper.VideoMapper;
import sk.posam.fsa.streaming.rest.api.MoviesApi;
import sk.posam.fsa.streaming.rest.dto.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
public class MovieRestController implements MoviesApi {

    private final MovieFacade movieFacade;
    private final MovieMapper movieMapper;
    private final VideoMapper videoMapper;

    public MovieRestController(MovieFacade movieFacade, MovieMapper movieMapper, VideoMapper videoMapper) {
        this.movieFacade = movieFacade;
        this.movieMapper = movieMapper;
        this.videoMapper = videoMapper;
    }

    @Override
    public ResponseEntity<List<MovieDto>> moviesFilterGet(
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

        List<Movie> filteredMovies = movieFacade.filter(filter);
        List<MovieDto> movieDtos = filteredMovies.stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(movieDtos);
    }

    @Override
    public ResponseEntity<MovieDto> createMovie(CreateMovieDto createMovieDto) {
        Movie movie = movieMapper.toEntity(createMovieDto);
        Movie savedMovie = movieFacade.create(movie);
        MovieDto dto = movieMapper.toDto(savedMovie);
        dto.setVideos(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Override
    public ResponseEntity<MovieDto> updateMovie(Long id, CreateMovieDto dto) {
        try {
            Movie movie = movieMapper.toEntity(dto);
            Movie updated = movieFacade.update(id, movie);
            return ResponseEntity.ok(movieMapper.toDto(updated));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        List<Movie> movies = movieFacade.findAll();
        List<MovieDto> dtos = movies.stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<MovieDto> getMovieById(Long id) {
        return movieFacade.get(id)
                .map(movieMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<Void> deleteMovie(Long id) {
        try {
            movieFacade.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
