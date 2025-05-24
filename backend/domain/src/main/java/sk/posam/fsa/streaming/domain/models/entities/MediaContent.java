package sk.posam.fsa.streaming.domain.models.entities;

import sk.posam.fsa.streaming.domain.models.enums.Genre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class MediaContent {
    protected Long id;
    protected String title;
    protected String slug;
    protected LocalDate releaseDate;
    protected Integer releaseYear;
    protected String description;
    protected List<Genre> genres = new ArrayList<>();
    protected String actors;
    protected String directors;
    protected String trailerUrl;
    protected List<String> countries = new ArrayList<>();
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
//    protected User createdBy;
//    protected User updatedBy;
    protected Float globalRating;
    protected List<Comment> comments = new ArrayList<>();
    protected Integer commentsTotal;
    protected String posterImg;
    protected String coverImg;
    protected String type;

    public MediaContent(Long id, String title, String slug, LocalDate releaseDate, Integer releaseYear, String description,
                        List<Genre> genres, String actors, String directors, String trailerUrl, List<String> countries,
                        LocalDateTime createdAt, LocalDateTime updatedAt, Float globalRating, List<Comment> comments,
                        Integer commentsTotal, String posterImg, String coverImg, String type) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.releaseDate = releaseDate;
        this.releaseYear = releaseYear;
        this.description = description;
        this.genres = genres;
        this.actors = actors;
        this.directors = directors;
        this.trailerUrl = trailerUrl;
        this.countries = countries;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
//        this.createdBy = createdBy;
//        this.updatedBy = updatedBy;
        this.globalRating = globalRating;
        this.comments = comments;
        this.commentsTotal = commentsTotal;
        this.posterImg = posterImg;
        this.coverImg = coverImg;
        this.type = type;
    }

    public MediaContent() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

//    public User getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(User createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public User getUpdatedBy() {
//        return updatedBy;
//    }
//
//    public void setUpdatedBy(User updatedBy) {
//        this.updatedBy = updatedBy;
//    }

    public Float getGlobalRating() {
        return globalRating;
    }

    public void setGlobalRating(Float globalRating) {
        this.globalRating = globalRating;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Integer getCommentsTotal() {
        return commentsTotal;
    }

    public void setCommentsTotal(Integer commentsTotal) {
        this.commentsTotal = commentsTotal;
    }

    public String getPosterImg() {
        return posterImg;
    }

    public void setPosterImg(String posterImg) {
        this.posterImg = posterImg;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void updateCommentsTotal() {
        this.commentsTotal = comments == null ? 0 : comments.size();
    }

    public String generateBaseSlug() {
        if (title == null) return null;
        return title.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}

