package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.Video;

import java.util.List;
import java.util.Optional;

public interface MovieFacade extends MediaContentFacade<Movie> {
    Movie addVideo(Long movieId, Video video);

    Movie updateVideo(Long movieId, Long videoId, Video updatedVideo);

    void removeVideo(Long movieId, Long videoId);

    Optional<Video> getVideoById(Long movieId, Long videoId);

    List<Video> getAllVideos(Long movieId);
}

