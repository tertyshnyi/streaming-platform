package sk.posam.fsa.streaming.domain.models.entities;

public class Video {
    private Episode episode;
    private Movie movie;
    private String resolution;
    private String url;

    private Video(Episode episode, Movie movie, String resolution, String url) {
        if ((episode == null && movie == null) || (episode != null && movie != null)) {
            throw new IllegalArgumentException("Video must be linked to either a movie or an episode, but not both.");
        }
        this.episode = episode;
        this.movie = movie;
        this.resolution = resolution;
        this.url = url;
    }

    public Video() {}

    public static Video ofEpisode(Episode episode, String resolution, String url) {
        return new Video(episode, null, resolution, url);
    }

    public static Video ofMovie(Movie movie, String resolution, String url) {
        return new Video(null, movie, resolution, url);
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
}
