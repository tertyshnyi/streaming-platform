package sk.posam.fsa.streaming.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.services.MovieFacade;
import sk.posam.fsa.streaming.mapper.MovieMapper;
import sk.posam.fsa.streaming.mapper.VideoMapper;
import sk.posam.fsa.streaming.rest.api.MoviesApi;
import sk.posam.fsa.streaming.rest.dto.CreateMovieDto;
import sk.posam.fsa.streaming.rest.dto.MovieDto;

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
    public ResponseEntity<MovieDto> createMovie(CreateMovieDto createMovieDto) {
        Movie movie = movieMapper.toEntity(createMovieDto);
        Movie savedMovie = movieFacade.create(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(movieMapper.toDto(savedMovie));
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
        return null;
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
