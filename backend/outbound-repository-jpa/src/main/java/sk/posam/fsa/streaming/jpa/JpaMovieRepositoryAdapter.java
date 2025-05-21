package sk.posam.fsa.streaming.jpa;

import org.springframework.stereotype.Repository;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.repositories.MovieRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaMovieRepositoryAdapter implements MovieRepository {

    private final MovieSpringDataRepository movieSpringDataRepository;

    public JpaMovieRepositoryAdapter(MovieSpringDataRepository movieSpringDataRepository) {
        this.movieSpringDataRepository = movieSpringDataRepository;
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return movieSpringDataRepository.findById(id);
    }

    @Override
    public Movie save(Movie movie) {
        return movieSpringDataRepository.save(movie);
    }
}
