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

}