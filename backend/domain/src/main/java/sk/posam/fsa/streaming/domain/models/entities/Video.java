package sk.posam.fsa.streaming.domain.models.entities;

import java.util.Objects;

public class Video {
    private Long id;
    private Episode episode;
    private Movie movie;
    private String resolution;
    private String url;

    private Video(Long id, Episode episode, Movie movie, String resolution, String url) {
        if ((episode == null && movie == null) || (episode != null && movie != null)) {
            throw new IllegalArgumentException("Video must be linked to either a movie or an episode, but not both.");
        }
        this.id = id;
        this.episode = episode;
        this.movie = movie;
        this.resolution = resolution;
        this.url = url;
    }

    public Video() {}

    public static Video ofEpisode(Long id, Episode episode, String resolution, String url) {
        return new Video(id, episode, null, resolution, url);
    }

    public static Video ofMovie(Long id, Movie movie, String resolution, String url) {
        return new Video(id, null, movie, resolution, url);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return id != null && id.equals(video.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
