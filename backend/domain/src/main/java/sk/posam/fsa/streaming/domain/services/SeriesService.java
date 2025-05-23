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
    public Series addEpisode(Long seriesId, Episode episode) {
        Series series = getSeriesOrThrow(seriesId);
        series.getEpisodes().add(episode);
        return seriesRepository.save(series);
    }

    @Override
    public Series updateEpisode(Long seriesId, Long episodeId, Episode updatedEpisode) {
        Series series = getSeriesOrThrow(seriesId);
        Episode episode = findEpisodeById(series, episodeId);

        episode.setTitle(updatedEpisode.getTitle());
        episode.setDuration(updatedEpisode.getDuration());
        episode.setVideos(updatedEpisode.getVideos());

        return seriesRepository.save(series);
    }

    @Override
    public void removeEpisode(Long seriesId, Long episodeId) {
        Series series = getSeriesOrThrow(seriesId);
        boolean removed = series.getEpisodes().removeIf(e -> e.getId().equals(episodeId));
        if (!removed) {
            throw new NoSuchElementException("Episode not found with id: " + episodeId);
        }
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
}
