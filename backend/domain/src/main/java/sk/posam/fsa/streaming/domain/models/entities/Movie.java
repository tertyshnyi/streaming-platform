package sk.posam.fsa.streaming.domain.models.entities;

import sk.posam.fsa.streaming.domain.models.enums.Genre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Movie extends MediaContent {
    private List<Video> videos = new ArrayList<>();
    private Integer duration;

    public Movie(Long id, String title, String slug, LocalDate releaseDate, Integer releaseYear, String description,
                 List<Genre> genres, String actors, String directors, String trailerUrl, List<String> countries,
                 LocalDateTime createdAt, LocalDateTime updatedAt, User createdBy, User updatedBy, String type,
                 Float globalRating, List<Comment> comments, Integer commentsTotal, String posterImg, String coverImg,
                 List<Video> videos, Integer duration) {
        super(id, title, slug, releaseDate, releaseYear, description, genres, actors, directors, trailerUrl,
                countries, createdAt, updatedAt, globalRating, comments, commentsTotal, posterImg, coverImg);
        this.videos = videos;
        this.duration = duration;
    }

    public Movie(List<Video> videos, Integer duration) {
        this.videos = videos;
        this.duration = duration;
    }

    public Movie() {}

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}