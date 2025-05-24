package sk.posam.fsa.streaming.domain.models.entities;

import sk.posam.fsa.streaming.domain.models.enums.Genre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Series extends MediaContent {
    private List<Episode> episodes = new ArrayList<>();
    private Integer episodeCount;
    private Integer avgDuration;

    public Series(Long id, String title, String slug, LocalDate releaseDate, Integer releaseYear, String description,
                  List<Genre> genres, String actors, String directors, String trailerUrl, List<String> countries,
                  LocalDateTime createdAt, LocalDateTime updatedAt, User createdBy, User updatedBy, String type,
                  Float globalRating, List<Comment> comments, Integer commentsTotal, String posterImg, String coverImg,
                  List<Episode> episodes, Integer episodeCount, Integer avgDuration) {
        super(id, title, slug, releaseDate, releaseYear, description, genres, actors, directors, trailerUrl,
                countries, createdAt, updatedAt, globalRating, comments, commentsTotal, posterImg, coverImg, type);
        this.episodes = episodes;
        this.episodeCount = episodeCount;
        this.avgDuration = avgDuration;
    }

    public Series(List<Episode> episodes, Integer episodeCount, Integer avgDuration) {
        this.episodes = episodes;
        this.episodeCount = episodeCount;
        this.avgDuration = avgDuration;
    }

    public Series(){}

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public Integer getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Integer avgDuration) {
        this.avgDuration = avgDuration;
    }
}

