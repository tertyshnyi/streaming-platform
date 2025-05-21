package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.Video;
import sk.posam.fsa.streaming.domain.repositories.MovieRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class MovieService extends MediaContentService<Movie> implements MovieFacade {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        super(movieRepository);
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie addVideo(Long movieId, Video video) {
        Movie movie = getMovieOrThrow(movieId);
        movie.getVideos().add(video);
        return movieRepository.save(movie);
    }

    @Override
    public Movie updateVideo(Long movieId, Long videoId, Video updatedVideo) {
        Movie movie = getMovieOrThrow(movieId);
        Optional<Video> videoOpt = movie.getVideos().stream()
                .filter(v -> v.getId().equals(videoId))
                .findFirst();

        if (videoOpt.isEmpty()) {
            throw new NoSuchElementException("Video not found with id: " + videoId);
        }

        Video video = videoOpt.get();
        video.setResolution(updatedVideo.getResolution());
        video.setUrl(updatedVideo.getUrl());

        return movieRepository.save(movie);
    }

    @Override
    public void removeVideo(Long movieId, Long videoId) {
        Movie movie = getMovieOrThrow(movieId);
        boolean removed = movie.getVideos().removeIf(v -> v.getId().equals(videoId));
        if (!removed) {
            throw new NoSuchElementException("Video not found with id: " + videoId);
        }
        movieRepository.save(movie);
    }

    @Override
    public Optional<Video> getVideoById(Long movieId, Long videoId) {
        Movie movie = getMovieOrThrow(movieId);
        return movie.getVideos().stream()
                .filter(v -> v.getId().equals(videoId))
                .findFirst();
    }

    @Override
    public List<Video> getAllVideos(Long movieId) {
        Movie movie = getMovieOrThrow(movieId);
        return movie.getVideos();
    }

    private Movie getMovieOrThrow(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + id));
    }
}
