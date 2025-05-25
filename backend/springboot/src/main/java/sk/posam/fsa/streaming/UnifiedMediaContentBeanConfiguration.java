package sk.posam.fsa.streaming;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.streaming.domain.services.*;

@Configuration
public class UnifiedMediaContentBeanConfiguration {

    @Bean
    public UnifiedMediaContentFacade unifiedMediaContentFacade(
            MovieFacade movieFacade,
            SeriesFacade seriesFacade
    ) {
        return new UnifiedMediaContentService(movieFacade, seriesFacade);
    }
}
