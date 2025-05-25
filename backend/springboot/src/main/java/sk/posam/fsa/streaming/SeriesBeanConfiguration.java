package sk.posam.fsa.streaming;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.streaming.domain.repositories.SeriesRepository;
import sk.posam.fsa.streaming.domain.services.SeriesFacade;
import sk.posam.fsa.streaming.domain.services.SeriesService;

@Configuration
public class SeriesBeanConfiguration {

    @Bean
    public SeriesFacade seriesFacade(SeriesRepository seriesRepository) {
        return new SeriesService(seriesRepository);
    }
}

