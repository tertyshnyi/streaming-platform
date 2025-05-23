package sk.posam.fsa.streaming.domain.models.entities;

import sk.posam.fsa.streaming.domain.models.enums.Genre;

import java.util.List;

public class MediaContentFilter {
    private List<Genre> genres;
    private List<String> countries;
    private List<Integer> releaseYears;
    private Float ratingFrom;
    private Float ratingTo;

    public MediaContentFilter(List<Genre> genres, List<String> countries, List<Integer> releaseYears, Float ratingFrom, Float ratingTo) {
        this.genres = genres;
        this.countries = countries;
        this.releaseYears = releaseYears;
        this.ratingFrom = ratingFrom;
        this.ratingTo = ratingTo;
    }

    public MediaContentFilter() {
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<Integer> getReleaseYears() {
        return releaseYears;
    }

    public void setReleaseYears(List<Integer> releaseYears) {
        this.releaseYears = releaseYears;
    }

    public Float getRatingFrom() {
        return ratingFrom;
    }

    public void setRatingFrom(Float ratingFrom) {
        this.ratingFrom = ratingFrom;
    }

    public Float getRatingTo() {
        return ratingTo;
    }

    public void setRatingTo(Float ratingTo) {
        this.ratingTo = ratingTo;
    }
}
