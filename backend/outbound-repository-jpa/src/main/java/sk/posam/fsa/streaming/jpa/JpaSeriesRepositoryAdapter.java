package sk.posam.fsa.streaming.jpa;

import org.springframework.stereotype.Repository;
import sk.posam.fsa.streaming.domain.models.entities.Series;
import sk.posam.fsa.streaming.domain.repositories.SeriesRepository;

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
    public Series save(Series series) {
        return seriesSpringDataRepository.save(series);
    }
}
