package sk.posam.fsa.streaming.domain.services;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.posam.fsa.streaming.domain.models.entities.MediaContent;
import sk.posam.fsa.streaming.domain.models.entities.MediaContentFilter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class MediaSearchServiceTest {

    private MovieFacade movieFacade;
    private SeriesFacade seriesFacade;
    private MediaSearchService mediaSearchService;

    @BeforeEach
    void setUp() {
        movieFacade = mock(MovieFacade.class);
        seriesFacade = mock(SeriesFacade.class);
        mediaSearchService = new MediaSearchService(movieFacade, seriesFacade);
    }

    @Test
    void filter_shouldCallCorrectFacadesBasedOnType() {
        MediaContentFilter movieFilter = new MediaContentFilter();
        movieFilter.setType("MOVIE");

        when(movieFacade.filter(any(MediaContentFilter.class)))
                .thenAnswer(invocation -> {
                    MediaContentFilter arg = invocation.getArgument(0);
                    if (arg.getType() == null || "MOVIE".equalsIgnoreCase(arg.getType())) {
                        return Arrays.asList(new TestMediaContent("Movie Filtered"));
                    }
                    return Arrays.asList();
                });

        when(seriesFacade.filter(any(MediaContentFilter.class)))
                .thenAnswer(invocation -> {
                    MediaContentFilter arg = invocation.getArgument(0);
                    if (arg.getType() == null || "SERIES".equalsIgnoreCase(arg.getType())) {
                        return Arrays.asList(new TestMediaContent("Series Filtered"));
                    }
                    return Arrays.asList();
                });

        List<MediaContent> moviesOnly = mediaSearchService.filter(movieFilter);
        assertEquals(1, moviesOnly.size());

        MediaContentFilter seriesFilter = new MediaContentFilter();
        seriesFilter.setType("SERIES");

        List<MediaContent> seriesOnly = mediaSearchService.filter(seriesFilter);
        assertEquals(1, seriesOnly.size());

        MediaContentFilter nullFilter = new MediaContentFilter();
        nullFilter.setType(null);

        List<MediaContent> both = mediaSearchService.filter(nullFilter);
        assertEquals(2, both.size());
    }

    @Test
    void getAllCountries_shouldReturnDistinctSortedCountries() {
        when(movieFacade.getDistinctCountries()).thenReturn(Arrays.asList("USA", "France"));
        when(seriesFacade.getDistinctCountries()).thenReturn(Arrays.asList("UK", "USA"));

        List<String> countries = mediaSearchService.getAllCountries();

        assertEquals(Arrays.asList("France", "UK", "USA"), countries);
    }

    @Test
    void getAllReleaseYears_shouldReturnDistinctSortedYearsDesc() {
        when(movieFacade.getDistinctReleaseYears()).thenReturn(Arrays.asList(2020, 2021));
        when(seriesFacade.getDistinctReleaseYears()).thenReturn(Arrays.asList(2019, 2021));

        List<Integer> years = mediaSearchService.getAllReleaseYears();

        assertEquals(Arrays.asList(2021, 2020, 2019), years);
    }

    @Test
    void findLatestMedia_shouldReturnSortedLimitedCombinedList() {
        LocalDateTime now = LocalDateTime.now();

        MediaContent movie1 = new TestMediaContent("Movie 1", now.minusDays(1));
        MediaContent movie2 = new TestMediaContent("Movie 2", now.minusDays(2));

        MediaContent series1 = new TestMediaContent("Series 1", now.minusHours(10));
        MediaContent series2 = new TestMediaContent("Series 2", now.minusDays(3));

        when(movieFacade.findLatestMedia(anyInt())).thenAnswer(invocation -> {
            int count = invocation.getArgument(0);
            List<MediaContent> allMovies = Arrays.asList(movie1, movie2);
            return allMovies.subList(0, Math.min(count, allMovies.size()));
        });

        when(seriesFacade.findLatestMedia(anyInt())).thenAnswer(invocation -> {
            int count = invocation.getArgument(0);
            List<MediaContent> allSeries = Arrays.asList(series1, series2);
            return allSeries.subList(0, Math.min(count, allSeries.size()));
        });

        List<MediaContent> latest = mediaSearchService.findLatestMedia(3);

        assertEquals(3, latest.size());
        assertEquals(series1, latest.get(0));
        assertEquals(movie1, latest.get(1));
        assertEquals(movie2, latest.get(2));
    }

    @Test
    void filter_shouldReturnEmptyListIfNoMatches() {
        MediaContentFilter filter = new MediaContentFilter();
        filter.setType("MOVIE");

        when(movieFacade.filter(any(MediaContentFilter.class))).thenReturn(Arrays.asList());
        when(seriesFacade.filter(any(MediaContentFilter.class))).thenReturn(Arrays.asList());

        List<MediaContent> result = mediaSearchService.filter(filter);
        assertTrue(result.isEmpty());
    }

    @Test
    void filter_shouldIgnoreCaseWhenCheckingType() {
        MediaContentFilter filter = new MediaContentFilter();
        filter.setType("movie");

        when(movieFacade.filter(any(MediaContentFilter.class))).thenAnswer(invocation -> {
            MediaContentFilter filterArg = invocation.getArgument(0);
            if ("movie".equalsIgnoreCase(filterArg.getType())) {
                return Arrays.asList(new TestMediaContent("Movie Lowercase"));
            }
            return Collections.emptyList();
        });

        when(seriesFacade.filter(any(MediaContentFilter.class))).thenAnswer(invocation -> {
            MediaContentFilter filterArg = invocation.getArgument(0);
            if ("series".equalsIgnoreCase(filterArg.getType())) {
                return Arrays.asList(new TestMediaContent("Series Filtered"));
            }
            return Collections.emptyList();
        });

        List<MediaContent> result = mediaSearchService.filter(filter);

        assertEquals(1, result.size());
        assertEquals("Movie Lowercase", result.get(0).getTitle());
    }

    @Test
    void getAllCountries_shouldHandleEmptyListsGracefully() {
        when(movieFacade.getDistinctCountries()).thenReturn(Arrays.asList());
        when(seriesFacade.getDistinctCountries()).thenReturn(Arrays.asList());

        List<String> countries = mediaSearchService.getAllCountries();
        assertTrue(countries.isEmpty());
    }

    @Test
    void getAllReleaseYears_shouldHandleEmptyListsGracefully() {
        when(movieFacade.getDistinctReleaseYears()).thenReturn(Arrays.asList());
        when(seriesFacade.getDistinctReleaseYears()).thenReturn(Arrays.asList());

        List<Integer> years = mediaSearchService.getAllReleaseYears();
        assertTrue(years.isEmpty());
    }

    @Test
    void findLatestMedia_shouldReturnEmptyListIfNoMedia() {
        when(movieFacade.findLatestMedia(anyInt())).thenReturn(Arrays.asList());
        when(seriesFacade.findLatestMedia(anyInt())).thenReturn(Arrays.asList());

        List<MediaContent> latest = mediaSearchService.findLatestMedia(5);
        assertTrue(latest.isEmpty());
    }

    @Test
    void findLatestMedia_shouldHandleCountLessThanZero() {
        when(movieFacade.findLatestMedia(anyInt())).thenReturn(Arrays.asList());
        when(seriesFacade.findLatestMedia(anyInt())).thenReturn(Arrays.asList());

        List<MediaContent> latest = mediaSearchService.findLatestMedia(-1);
        assertTrue(latest.isEmpty());
    }

    @Test
    void searchByText_shouldReturnEmptyListIfNoMatches() {
        when(movieFacade.searchByText(anyString())).thenReturn(Arrays.asList());
        when(seriesFacade.searchByText(anyString())).thenReturn(Arrays.asList());

        List<MediaContent> result = mediaSearchService.searchByText("nonexistent");
        assertTrue(result.isEmpty());
    }

    // Вспомогательный класс для теста
    static class TestMediaContent extends MediaContent {
        private final String title;
        private final LocalDateTime createdAt;

        TestMediaContent(String title) {
            this(title, LocalDateTime.now());
        }

        TestMediaContent(String title, LocalDateTime createdAt) {
            this.title = title;
            this.createdAt = createdAt;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }
}
