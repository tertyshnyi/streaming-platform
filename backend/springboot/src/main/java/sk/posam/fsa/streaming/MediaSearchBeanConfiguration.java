package sk.posam.fsa.streaming;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.streaming.domain.services.MediaSearchService;
import sk.posam.fsa.streaming.domain.services.MovieFacade;
import sk.posam.fsa.streaming.domain.services.SeriesFacade;

@Configuration
public class MediaSearchBeanConfiguration {

    @Bean
    public MediaSearchService mediaSearchService(MovieFacade movieFacade, SeriesFacade seriesFacade) {
        return new MediaSearchService(movieFacade, seriesFacade);
    }
}


