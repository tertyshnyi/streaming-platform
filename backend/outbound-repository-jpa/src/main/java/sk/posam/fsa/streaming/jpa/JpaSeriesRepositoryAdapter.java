package sk.posam.fsa.streaming.jpa;

import org.springframework.stereotype.Repository;
import sk.posam.fsa.streaming.domain.models.entities.MediaContentFilter;
import sk.posam.fsa.streaming.domain.models.entities.Movie;
import sk.posam.fsa.streaming.domain.models.entities.Series;
import sk.posam.fsa.streaming.domain.models.enums.Genre;
import sk.posam.fsa.streaming.domain.repositories.SeriesRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaSeriesRepositoryAdapter implements SeriesRepository {

    private final SeriesSpringDataRepository seriesSpringDataRepository;

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
        return null;
    }

}
