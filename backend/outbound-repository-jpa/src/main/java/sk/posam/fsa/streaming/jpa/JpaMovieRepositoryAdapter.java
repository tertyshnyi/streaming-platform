package sk.posam.fsa.streaming.jpa;

import org.springframework.stereotype.Repository;
import sk.posam.fsa.streaming.domain.models.entities.MediaContentFilter;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.enums.Genre;
import sk.posam.fsa.streaming.domain.repositories.MovieRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaMovieRepositoryAdapter implements MovieRepository {

    private final MovieSpringDataRepository movieSpringDataRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public JpaMovieRepositoryAdapter(MovieSpringDataRepository movieSpringDataRepository) {
        this.movieSpringDataRepository = movieSpringDataRepository;
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
    public List<Movie> findAll() {
        return movieSpringDataRepository.findAll();
    }

    @Override
    public List<Movie> filter(MediaContentFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> query = cb.createQuery(Movie.class);
        Root<Movie> root = query.from(Movie.class);

        Predicate predicate = cb.conjunction();

        if (filter.getGenres() != null && !filter.getGenres().isEmpty()) {
            Join<Movie, Genre> genresJoin = root.join("genres");
            predicate = cb.and(predicate, genresJoin.in(filter.getGenres()));
        }

        if (filter.getCountries() != null && !filter.getCountries().isEmpty()) {
            Join<Movie, String> countriesJoin = root.join("countries");
            predicate = cb.and(predicate, countriesJoin.in(filter.getCountries()));
        }

        if (filter.getReleaseYears() != null && !filter.getReleaseYears().isEmpty()) {
            predicate = cb.and(predicate, root.get("releaseYear").in(filter.getReleaseYears()));
        }

        if (filter.getRatingFrom() != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("globalRating"), filter.getRatingFrom()));
        }

        if (filter.getRatingTo() != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("globalRating"), filter.getRatingTo()));
        }

        query.where(predicate);
        query.distinct(true);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Movie> searchByText(String text) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> query = cb.createQuery(Movie.class);
        Root<Movie> root = query.from(Movie.class);

        String likePattern = "%" + text.toLowerCase() + "%";

        Predicate titleMatch = cb.like(cb.lower(root.get("title")), likePattern);
        Predicate descriptionMatch = cb.like(cb.lower(root.get("description")), likePattern);
        query.where(cb.or(titleMatch, descriptionMatch));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void delete(Long id) {
        movieSpringDataRepository.deleteById(id);
    }

    @Override
    public List<String> findDistinctCountries() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<Movie> root = query.from(Movie.class);
        Join<Movie, String> countriesJoin = root.join("countries");
        query.select(countriesJoin).distinct(true);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Integer> findDistinctReleaseYears() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
        Root<Movie> root = query.from(Movie.class);
        query.select(root.get("releaseYear")).distinct(true);
        query.where(cb.isNotNull(root.get("releaseYear")));
        return entityManager.createQuery(query).getResultList();
    }
}
