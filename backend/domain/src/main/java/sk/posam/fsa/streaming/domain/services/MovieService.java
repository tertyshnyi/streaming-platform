package sk.posam.fsa.streaming.domain.services;

import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.repositories.MovieRepository;

import java.util.List;

public class MovieService extends MediaContentService<Movie> implements MovieFacade {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        super(movieRepository, movieRepository);
        this.movieRepository = movieRepository;
    }

    @Override
    public void incrementCommentsTotal(Long id) {
        Movie movie = movieRepository.get(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        movie.setCommentsTotal(movie.getCommentsTotal() + 1);
        movieRepository.update(id, movie);
    }

    @Override
    public void decrementCommentsTotal(Long id, int decrementBy) {
        Movie movie = movieRepository.get(id).orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        int current = movie.getCommentsTotal();
        movie.setCommentsTotal(Math.max(0, current - decrementBy));
        movieRepository.update(id, movie);
    }

    @Override
    public List<String> getDistinctCountries() {
        return movieRepository.findDistinctCountries();
    }

    @Override
    public List<Integer> getDistinctReleaseYears() {
        return movieRepository.findDistinctReleaseYears();
    }
}