package sk.posam.fsa.streaming;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.streaming.domain.repositories.CommentRepository;
import sk.posam.fsa.streaming.domain.services.CommentFacade;
import sk.posam.fsa.streaming.domain.services.CommentService;
import sk.posam.fsa.streaming.domain.services.UnifiedMediaContentFacade;

@Configuration
public class CommentBeanConfiguration {

    @Bean
    public CommentFacade commentFacade(CommentRepository commentRepository,
                                       UnifiedMediaContentFacade mediaContentService) {
        return new CommentService(commentRepository, mediaContentService);
    }

}

