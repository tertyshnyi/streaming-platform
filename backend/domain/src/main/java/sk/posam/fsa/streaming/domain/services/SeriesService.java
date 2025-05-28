package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.Episode;
import sk.posam.fsa.streaming.domain.models.entities.Series;
import sk.posam.fsa.streaming.domain.repositories.SeriesRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class SeriesService extends MediaContentService<Series> implements SeriesFacade {

    private final SeriesRepository seriesRepository;

    public SeriesService(SeriesRepository seriesRepository) {
        super(seriesRepository, seriesRepository);
        this.seriesRepository = seriesRepository;
    }

    @Override
    public Series create(Series entity) {
        recalculateEpisodeStats(entity);
        Series created = super.create(entity);
        return seriesRepository.update(created.getId(), created);
    }

    @Override
    public Series update(Long id, Series entity) {
        recalculateEpisodeStats(entity);
        Series updated = super.update(id, entity);
        return seriesRepository.update(id, updated);
    }

    @Override
    public void recalculateEpisodeStats(Series series) {
        if (series.getEpisodes() == null || series.getEpisodes().isEmpty()) {
            series.setEpisodeCount(0);
            series.setAvgDuration(0);
        } else {
            int count = series.getEpisodes().size();
            int totalDuration = series.getEpisodes().stream()
                    .filter(e -> e.getDuration() != null)
                    .mapToInt(Episode::getDuration)
                    .sum();
            series.setEpisodeCount(count);
            series.setAvgDuration(totalDuration / count);
        }
    }

    @Override
    public Series addEpisode(Long seriesId, Episode episode) {
        Series series = getSeriesOrThrow(seriesId);
        series.getEpisodes().add(episode);
        recalculateEpisodeStats(series);
        return seriesRepository.save(series);
    }

    @Override
    public Series updateEpisode(Long seriesId, Long episodeId, Episode updatedEpisode) {
        Series series = getSeriesOrThrow(seriesId);
        Episode episode = findEpisodeById(series, episodeId);

        episode.setTitle(updatedEpisode.getTitle());
        episode.setDuration(updatedEpisode.getDuration());
        episode.setVideos(updatedEpisode.getVideos());

        recalculateEpisodeStats(series);

        return seriesRepository.save(series);
    }

    @Override
    public void removeEpisode(Long seriesId, Long episodeId) {
        Series series = getSeriesOrThrow(seriesId);
        boolean removed = series.getEpisodes().removeIf(e -> e.getId().equals(episodeId));
        if (!removed) {
            throw new NoSuchElementException("Episode not found with id: " + episodeId);
        }

        recalculateEpisodeStats(series);

        seriesRepository.save(series);
    }

    @Override
    public Optional<Episode> getEpisodeById(Long seriesId, Long episodeId) {
        Series series = getSeriesOrThrow(seriesId);
        return series.getEpisodes().stream()
                .filter(e -> e.getId().equals(episodeId))
                .findFirst();
    }

    @Override
    public List<Episode> getAllEpisodes(Long seriesId) {
        return getSeriesOrThrow(seriesId).getEpisodes();
    }

    @Override
    public List<String> getDistinctCountries() {
        return seriesRepository.findDistinctCountries();
    }

    @Override
    public List<Integer> getDistinctReleaseYears() {
        return seriesRepository.findDistinctReleaseYears();
    }

    private Series getSeriesOrThrow(Long id) {
        return seriesRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Series not found with id: " + id));
    }

    private Episode findEpisodeById(Series series, Long episodeId) {
        return series.getEpisodes().stream()
                .filter(e -> e.getId().equals(episodeId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Episode not found with id: " + episodeId));
    }

    @Override
    public void incrementCommentsTotal(Long id) {
        Series series = seriesRepository.get(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        series.setCommentsTotal(series.getCommentsTotal() + 1);
        seriesRepository.update(id, series);
    }

    @Override
    public void decrementCommentsTotal(Long id, int decrementBy) {
        Series series = seriesRepository.get(id)
                .orElseThrow(() -> new IllegalArgumentException("Series not found"));
        int current = series.getCommentsTotal();
        series.setCommentsTotal(Math.max(0, current - decrementBy));
        seriesRepository.update(id, series);
    }
}
