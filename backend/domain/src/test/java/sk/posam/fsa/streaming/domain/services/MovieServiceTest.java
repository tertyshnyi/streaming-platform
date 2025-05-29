package sk.posam.fsa.streaming.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.repositories.MovieRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    private MovieRepository movieRepository;
    private MovieService movieService;

    @BeforeEach
    void setup() {
        movieRepository = mock(MovieRepository.class);
        movieService = new MovieService(movieRepository);
    }

    @Test
    void incrementCommentsTotal_incrementsCount() {
        Long movieId = 1L;
        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setCommentsTotal(5);

        when(movieRepository.get(movieId)).thenReturn(Optional.of(movie));
        when(movieRepository.update(eq(movieId), any(Movie.class))).thenReturn(movie);

        movieService.incrementCommentsTotal(movieId);

        // Проверяем, что commentsTotal увеличился на 1
        assertEquals(6, movie.getCommentsTotal());

        verify(movieRepository).get(movieId);
        verify(movieRepository).update(eq(movieId), eq(movie));
    }

    @Test
    void incrementCommentsTotal_movieNotFound_throwsException() {
        Long movieId = 2L;

        when(movieRepository.get(movieId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            movieService.incrementCommentsTotal(movieId);
        });

        assertEquals("Movie not found", ex.getMessage());

        verify(movieRepository).get(movieId);
        verify(movieRepository, never()).update(anyLong(), any());
    }

    @Test
    void decrementCommentsTotal_decrementsCountButNotBelowZero() {
        Long movieId = 3L;
        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setCommentsTotal(3);

        when(movieRepository.get(movieId)).thenReturn(Optional.of(movie));
        when(movieRepository.update(eq(movieId), any(Movie.class))).thenReturn(movie);

        movieService.decrementCommentsTotal(movieId, 2);
        assertEquals(1, movie.getCommentsTotal());

        movieService.decrementCommentsTotal(movieId, 5);
        assertEquals(0, movie.getCommentsTotal());  // Не меньше нуля

        verify(movieRepository, times(2)).get(movieId);
        verify(movieRepository, times(2)).update(eq(movieId), eq(movie));
    }

    @Test
    void getDistinctCountries_callsRepository() {
        when(movieRepository.findDistinctCountries()).thenReturn(java.util.List.of("USA", "France"));

        var countries = movieService.getDistinctCountries();

        assertEquals(2, countries.size());
        assertTrue(countries.contains("USA"));
        assertTrue(countries.contains("France"));

        verify(movieRepository).findDistinctCountries();
    }

    @Test
    void getDistinctReleaseYears_callsRepository() {
        when(movieRepository.findDistinctReleaseYears()).thenReturn(java.util.List.of(1999, 2005, 2020));

        var years = movieService.getDistinctReleaseYears();

        assertEquals(3, years.size());
        assertTrue(years.contains(1999));
        assertTrue(years.contains(2005));
        assertTrue(years.contains(2020));

        verify(movieRepository).findDistinctReleaseYears();
    }
}
