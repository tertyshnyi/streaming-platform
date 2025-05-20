package sk.posam.fsa.streaming.domain.models.entities;

import java.time.LocalDateTime;

public class WatchHistory {
    private User user;
    private Movie movie;
    private Episode episode;
    private Integer watchedTime;
    private LocalDateTime watchedAt;

    public WatchHistory(User user, Movie movie, Episode episode, Integer watchedTime, LocalDateTime watchedAt) {
        if ((episode == null && movie == null) || (episode != null && movie != null)) {
            throw new IllegalArgumentException("WatchList must be linked to either a movie or an episode, but not both.");
        }
        this.user = user;
        this.movie = movie;
        this.episode = episode;
        this.watchedTime = watchedTime;
        this.watchedAt = watchedAt;
    }

    public WatchHistory() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    public Integer getWatchedTime() {
        return watchedTime;
    }

    public void setWatchedTime(Integer watchedTime) {
        this.watchedTime = watchedTime;
    }

    public LocalDateTime getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(LocalDateTime watchedAt) {
        this.watchedAt = watchedAt;
    }
}
