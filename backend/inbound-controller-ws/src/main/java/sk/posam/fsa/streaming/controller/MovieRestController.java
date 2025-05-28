package sk.posam.fsa.streaming.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.User;
import sk.posam.fsa.streaming.domain.services.MovieFacade;
import sk.posam.fsa.streaming.domain.services.UserFacade;
import sk.posam.fsa.streaming.mapper.MovieMapper;
import sk.posam.fsa.streaming.mapper.VideoMapper;
import sk.posam.fsa.streaming.rest.api.MoviesApi;
import sk.posam.fsa.streaming.rest.dto.*;
import sk.posam.fsa.streaming.security.CurrentUserDetailService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
public class MovieRestController implements MoviesApi {

    private final MovieFacade movieFacade;
    private final MovieMapper movieMapper;
    private final VideoMapper videoMapper;
    private final UserFacade userFacade;
    private final CurrentUserDetailService currentUserDetailService;

    public MovieRestController(MovieFacade movieFacade, MovieMapper movieMapper, VideoMapper videoMapper, UserFacade userFacade, CurrentUserDetailService currentUserDetailService) {
        this.movieFacade = movieFacade;
        this.movieMapper = movieMapper;
        this.videoMapper = videoMapper;
        this.userFacade = userFacade;
        this.currentUserDetailService = currentUserDetailService;
    }

    @Override
    public ResponseEntity<MovieDto> createMovie(CreateMovieDto createMovieDto) {
        UserDto userDto = currentUserDetailService.getCurrentUser();

        User user = userFacade.findByKeycloakId(userDto.getKeycloakId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Movie movie = movieMapper.toEntity(createMovieDto, user, user);
        Movie saved = movieFacade.create(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(movieMapper.toDto(saved));
    }

    @Override
    public ResponseEntity<MovieDto> updateMovie(Long id, CreateMovieDto dto) {
        try {
            UserDto userDto = currentUserDetailService.getCurrentUser();

            User user = userFacade.findByKeycloakId(userDto.getKeycloakId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Movie existingMovie = movieFacade.get(id)
                    .orElseThrow(() -> new NoSuchElementException("Movie not found"));

            movieMapper.updateEntity(existingMovie, dto, user);

            Movie updated = movieFacade.update(id, existingMovie);
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
