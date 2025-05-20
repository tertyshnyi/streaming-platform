package sk.posam.fsa.streaming.domain.models.entities;

import java.util.List;
import java.util.Optional;

public class Episode {
    private Long id;
    private Series series;
    private Optional<String> title;
    private Integer duration;
    private List<Video> videos;

    public Episode(Long id, Series series, Optional<String> title, Integer duration, List<Video> videos) {
        this.id = id;
        this.series = series;
        this.title = title;
        this.duration = duration;
        this.videos = videos;
    }

    public Episode() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}

