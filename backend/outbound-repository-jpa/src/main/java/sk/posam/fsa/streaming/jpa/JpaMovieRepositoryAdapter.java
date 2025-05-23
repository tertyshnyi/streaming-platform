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
    public Optional<Movie> findBySlug(String slug) {
        return movieSpringDataRepository.findBySlug(slug);
    }

    @Override
    public Optional<Movie> get(Long id) {
        return movieSpringDataRepository.findById(id);
    }

    @Override
    public Movie create(Movie entity) {
        return movieSpringDataRepository.save(entity);
    }

    @Override
    public Movie update(Long id, Movie entity) {
        return movieSpringDataRepository.save(entity);
    }

    @Override
    public Movie save(Movie movie) {
        return movieSpringDataRepository.save(movie);
    }

    @Override
    public List<Movie> findAll() {
        return movieSpringDataRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        movieSpringDataRepository.deleteById(id);
    }
}
