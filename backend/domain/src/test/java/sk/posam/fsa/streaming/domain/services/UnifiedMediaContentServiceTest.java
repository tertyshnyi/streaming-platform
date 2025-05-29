package sk.posam.fsa.streaming.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.Series;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnifiedMediaContentServiceTest {

    private MovieFacade movieFacade;
    private SeriesFacade seriesFacade;
    private UnifiedMediaContentService service;

    @BeforeEach
    void setUp() {
        movieFacade = mock(MovieFacade.class);
        seriesFacade = mock(SeriesFacade.class);
        service = new UnifiedMediaContentService(movieFacade, seriesFacade);
    }

    @Test
    void get_returnsMovieIfPresent() {
        Movie movie = new Movie();
        movie.setId(1L);

        when(movieFacade.get(1L)).thenReturn(Optional.of(movie));
        when(seriesFacade.get(1L)).thenReturn(Optional.empty());

        Optional<MediaContent> result = service.get(1L);

        assertTrue(result.isPresent());
        assertEquals(movie, result.get());
        verify(movieFacade).get(1L);
        verify(seriesFacade, never()).get(1L);
    }

    @Test
    void get_returnsSeriesIfMovieNotPresent() {
        Series series = new Series();
        series.setId(2L);

        when(movieFacade.get(2L)).thenReturn(Optional.empty());
        when(seriesFacade.get(2L)).thenReturn(Optional.of(series));

        Optional<MediaContent> result = service.get(2L);

        assertTrue(result.isPresent());
        assertEquals(series, result.get());
        verify(movieFacade).get(2L);
        verify(seriesFacade).get(2L);
    }

    @Test
    void get_returnsEmptyIfNeitherPresent() {
        when(movieFacade.get(3L)).thenReturn(Optional.empty());
        when(seriesFacade.get(3L)).thenReturn(Optional.empty());

        Optional<MediaContent> result = service.get(3L);

        assertTrue(result.isEmpty());
        verify(movieFacade).get(3L);
        verify(seriesFacade).get(3L);
    }

    @Test
    void incrementCommentsTotal_callsMovieFacadeIfMoviePresent() {
        when(movieFacade.get(1L)).thenReturn(Optional.of(new Movie()));
        service.incrementCommentsTotal(1L);
        verify(movieFacade).incrementCommentsTotal(1L);
        verify(seriesFacade, never()).incrementCommentsTotal(anyLong());
    }

    @Test
    void incrementCommentsTotal_callsSeriesFacadeIfMovieNotPresentButSeriesPresent() {
        when(movieFacade.get(2L)).thenReturn(Optional.empty());
        when(seriesFacade.get(2L)).thenReturn(Optional.of(new Series()));

        service.incrementCommentsTotal(2L);

        verify(movieFacade).get(2L);
        verify(seriesFacade).incrementCommentsTotal(2L);
        verify(movieFacade, never()).incrementCommentsTotal(anyLong());
    }

    @Test
    void decrementCommentsTotal_callsMovieFacadeIfMoviePresent() {
        when(movieFacade.get(1L)).thenReturn(Optional.of(new Movie()));

        service.decrementCommentsTotal(1L, 2);

        verify(movieFacade).decrementCommentsTotal(1L, 2);
        verify(seriesFacade, never()).decrementCommentsTotal(anyLong(), anyInt());
    }

    @Test
    void decrementCommentsTotal_callsSeriesFacadeIfMovieNotPresentButSeriesPresent() {
        when(movieFacade.get(2L)).thenReturn(Optional.empty());
        when(seriesFacade.get(2L)).thenReturn(Optional.of(new Series()));

        service.decrementCommentsTotal(2L, 5);

        verify(seriesFacade).decrementCommentsTotal(2L, 5);
        verify(movieFacade, never()).decrementCommentsTotal(anyLong(), anyInt());
    }
}
