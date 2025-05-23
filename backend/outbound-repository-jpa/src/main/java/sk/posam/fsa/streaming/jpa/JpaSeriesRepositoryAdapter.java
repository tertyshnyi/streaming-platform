package sk.posam.fsa.streaming.jpa;

import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import sk.posam.fsa.streaming.domain.models.entities.MediaContentFilter;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.Series;
import sk.posam.fsa.streaming.domain.models.enums.Genre;
import sk.posam.fsa.streaming.domain.repositories.SeriesRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaSeriesRepositoryAdapter implements SeriesRepository {

    private final SeriesSpringDataRepository seriesSpringDataRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public JpaSeriesRepositoryAdapter(SeriesSpringDataRepository seriesSpringDataRepository) {
        this.seriesSpringDataRepository = seriesSpringDataRepository;
    }

    @Override
    public Optional<Series> findById(Long id) {
        return seriesSpringDataRepository.findById(id);
    }

    @Override
    public Optional<Series> findBySlug(String slug) {
        return seriesSpringDataRepository.findBySlug(slug);
    }

    @Override
    public Series save(Series series) {
        return seriesSpringDataRepository.save(series);
    }

    @Override
    public Optional<Series> get(Long id) {
        return seriesSpringDataRepository.findById(id);
    }

    @Override
    public Series create(Series entity) {
        return seriesSpringDataRepository.save(entity);
    }

    @Override
    public Series update(Long id, Series entity) {
        return seriesSpringDataRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        seriesSpringDataRepository.deleteById(id);
    }

    @Override
    public List<Series> findAll() {
        return seriesSpringDataRepository.findAll();
    }

    @Override
    public List<Series> filter(MediaContentFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Series> query = cb.createQuery(Series.class);
        Root<Series> root = query.from(Series.class);

        Predicate predicate = cb.conjunction();

        if (filter.getGenres() != null && !filter.getGenres().isEmpty()) {
            Join<Series, Genre> genresJoin = root.join("genres");
            predicate = cb.and(predicate, genresJoin.in(filter.getGenres()));
        }

        if (filter.getCountries() != null && !filter.getCountries().isEmpty()) {
            Join<Series, String> countriesJoin = root.join("countries");
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

}
