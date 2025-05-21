package sk.posam.fsa.streaming;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.streaming.domain.repositories.MovieRepository;
import sk.posam.fsa.streaming.domain.services.MovieFacade;
import sk.posam.fsa.streaming.domain.services.MovieService;

@Configuration
public class MovieBeanConfiguration {

    @Bean
    public MovieFacade movieFacade(MovieRepository movieRepository) {
        return new MovieService(movieRepository);
    }
}
