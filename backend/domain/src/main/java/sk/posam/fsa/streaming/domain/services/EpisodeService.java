package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.Episode;
import sk.posam.fsa.streaming.domain.models.entities.Series;
import sk.posam.fsa.streaming.domain.models.entities.Video;
import sk.posam.fsa.streaming.domain.repositories.SeriesRepository;

import java.util.NoSuchElementException;

public class EpisodeService implements EpisodeFacade {

    private final SeriesRepository seriesRepository;

    public EpisodeService(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    public Episode addVideo(Long seriesId, Long episodeId, Video video) {
        Series series = getSeriesOrThrow(seriesId);
        Episode episode = findEpisodeById(series, episodeId);
        episode.getVideos().add(video);
        seriesRepository.save(series);
        return episode;
    }

    public Episode updateVideo(Long seriesId, Long episodeId, Long videoId, Video updatedVideo) {
        Series series = getSeriesOrThrow(seriesId);
        Episode episode = findEpisodeById(series, episodeId);
        Video video = episode.getVideos().stream()
                .filter(v -> v.getId().equals(videoId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Video not found with id: " + videoId));

        video.setUrl(updatedVideo.getUrl());
        video.setResolution(updatedVideo.getResolution());

        seriesRepository.save(series);
        return episode;
    }

    public Episode removeVideo(Long seriesId, Long episodeId, Long videoId) {
        Series series = getSeriesOrThrow(seriesId);
        Episode episode = findEpisodeById(series, episodeId);
        boolean removed = episode.getVideos().removeIf(v -> v.getId().equals(videoId));
        if (!removed) throw new NoSuchElementException("Video not found with id: " + videoId);
        seriesRepository.save(series);
        return episode;
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

