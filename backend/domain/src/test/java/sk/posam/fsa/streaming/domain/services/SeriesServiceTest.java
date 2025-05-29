package sk.posam.fsa.streaming.domain.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.posam.fsa.streaming.domain.models.entities.Episode;
import sk.posam.fsa.streaming.domain.models.entities.Series;
import sk.posam.fsa.streaming.domain.repositories.SeriesRepository;
import sk.posam.fsa.streaming.domain.repositories.SlugRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeriesServiceTest {

    @Mock
    private SeriesRepository seriesRepository;

    @Mock
    private SlugRepository slugRepository;

    @InjectMocks
    private SeriesService seriesService;

    private final LocalDateTime fixedNow = LocalDateTime.of(2025, 1, 1, 12, 0);

    @Test
    void create_recalculatesAndSavesSeries() {
        Series inputSeries = new Series();
        inputSeries.setCreatedAt(null);
        inputSeries.setTitle("My Test Series");

        lenient().when(slugRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        Series savedSeries = new Series();
        savedSeries.setId(42L);
        savedSeries.setCreatedAt(fixedNow);
        savedSeries.setTitle(inputSeries.getTitle());
        savedSeries.setSlug("my-test-series");

        when(seriesRepository.create(any(Series.class))).thenReturn(savedSeries);

        when(seriesRepository.update(eq(savedSeries.getId()), any(Series.class)))
                .thenAnswer(invocation -> invocation.getArgument(1));

        Series result = seriesService.create(inputSeries);

        assertNotNull(result);
        assertEquals(savedSeries.getId(), result.getId());
        assertNotNull(result.getSlug());
        assertFalse(result.getSlug().isEmpty());

        verify(seriesRepository, times(1)).create(any(Series.class));
        verify(seriesRepository, times(1)).update(eq(savedSeries.getId()), any(Series.class));
    }

    @Test
    void addEpisode_addsEpisodeAndRecalculates() {
        Series series = new Series();
        series.setId(1L);
        series.setEpisodes(new ArrayList<>());
        Episode episode = new Episode();
        episode.setDuration(30);

        when(seriesRepository.findById(series.getId())).thenReturn(Optional.of(series));
        when(seriesRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Series updated = seriesService.addEpisode(series.getId(), episode);

        assertEquals(1, updated.getEpisodeCount());
        assertEquals(30, updated.getAvgDuration());
        assertTrue(updated.getEpisodes().contains(episode));
    }

    @Test
    void updateEpisode_updatesEpisodeAndRecalculates() {
        Episode episode = new Episode();
        episode.setId(1L);
        episode.setDuration(25);

        Series series = new Series();
        series.setId(1L);
        List<Episode> episodes = new ArrayList<>();
        episodes.add(episode);
        series.setEpisodes(episodes);

        Episode updatedEpisode = new Episode();
        updatedEpisode.setTitle("New Title");
        updatedEpisode.setDuration(40);

        when(seriesRepository.findById(series.getId())).thenReturn(Optional.of(series));
        when(seriesRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Series result = seriesService.updateEpisode(series.getId(), episode.getId(), updatedEpisode);

        Episode ep = result.getEpisodes().get(0);
        assertEquals("New Title", ep.getTitle());
        assertEquals(40, ep.getDuration());
        assertEquals(40, result.getAvgDuration());
    }

    @Test
    void removeEpisode_removesEpisodeAndRecalculates() {
        Episode episode = new Episode();
        episode.setId(1L);
        episode.setDuration(20);

        Series series = new Series();
        series.setId(1L);
        List<Episode> episodes = new ArrayList<>();
        episodes.add(episode);
        series.setEpisodes(episodes);

        when(seriesRepository.findById(series.getId())).thenReturn(Optional.of(series));
        when(seriesRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        seriesService.removeEpisode(series.getId(), episode.getId());

        assertTrue(series.getEpisodes().isEmpty());
        assertEquals(0, series.getEpisodeCount());
    }

    @Test
    void removeEpisode_throwsIfEpisodeNotFound() {
        Series series = new Series();
        series.setId(1L);
        series.setEpisodes(new ArrayList<>());

        when(seriesRepository.findById(series.getId())).thenReturn(Optional.of(series));

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> {
            seriesService.removeEpisode(series.getId(), 99L);
        });

        assertTrue(ex.getMessage().contains("Episode not found"));
    }

    @Test
    void incrementCommentsTotal_incrementsCount() {
        Series series = new Series();
        series.setId(1L);
        series.setCommentsTotal(2);

        when(seriesRepository.get(series.getId())).thenReturn(Optional.of(series));
        when(seriesRepository.update(anyLong(), any())).thenReturn(series);

        seriesService.incrementCommentsTotal(series.getId());

        assertEquals(3, series.getCommentsTotal());
        verify(seriesRepository).update(eq(series.getId()), eq(series));
    }

    @Test
    void decrementCommentsTotal_decrementsButNotBelowZero() {
        Series series = new Series();
        series.setId(1L);
        series.setCommentsTotal(3);

        when(seriesRepository.get(series.getId())).thenReturn(Optional.of(series));
        when(seriesRepository.update(anyLong(), any())).thenReturn(series);

        seriesService.decrementCommentsTotal(series.getId(), 2);
        assertEquals(1, series.getCommentsTotal());

        seriesService.decrementCommentsTotal(series.getId(), 5);
        assertEquals(0, series.getCommentsTotal());
    }

    @Test
    void getEpisodeById_returnsEpisodeIfFound() {
        Episode episode = new Episode();
        episode.setId(10L);

        Series series = new Series();
        series.setId(1L);
        series.setEpisodes(List.of(episode));

        when(seriesRepository.findById(series.getId())).thenReturn(Optional.of(series));

        Optional<Episode> found = seriesService.getEpisodeById(series.getId(), episode.getId());
        assertTrue(found.isPresent());
        assertEquals(episode, found.get());
    }

    @Test
    void getAllEpisodes_returnsList() {
        Episode episode = new Episode();
        episode.setId(10L);

        Series series = new Series();
        series.setId(1L);
        series.setEpisodes(List.of(episode));

        when(seriesRepository.findById(series.getId())).thenReturn(Optional.of(series));

        List<Episode> episodes = seriesService.getAllEpisodes(series.getId());
        assertEquals(1, episodes.size());
        assertEquals(episode, episodes.get(0));
    }

    @Test
    void getDistinctCountries_callsRepository() {
        when(seriesRepository.findDistinctCountries()).thenReturn(List.of("USA", "UK"));

        List<String> countries = seriesService.getDistinctCountries();

        assertEquals(2, countries.size());
        assertTrue(countries.contains("USA"));
        assertTrue(countries.contains("UK"));
        verify(seriesRepository).findDistinctCountries();
    }

    @Test
    void getDistinctReleaseYears_callsRepository() {
        when(seriesRepository.findDistinctReleaseYears()).thenReturn(List.of(2010, 2015));

        List<Integer> years = seriesService.getDistinctReleaseYears();

        assertEquals(2, years.size());
        assertTrue(years.contains(2010));
        assertTrue(years.contains(2015));
        verify(seriesRepository).findDistinctReleaseYears();
    }
}
