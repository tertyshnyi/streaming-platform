package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.Episode;
import sk.posam.fsa.streaming.domain.models.entities.Video;

public interface EpisodeFacade {
    Episode addVideo(Long seriesId, Long episodeId, Video video);
    Episode removeVideo(Long seriesId, Long episodeId, Long videoId);
}
