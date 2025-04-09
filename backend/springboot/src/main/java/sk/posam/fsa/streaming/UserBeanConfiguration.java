package sk.posam.fsa.streaming;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.streaming.domain.repositories.UserRepository;
import sk.posam.fsa.streaming.domain.services.UserFacade;
import sk.posam.fsa.streaming.domain.services.UserService;

@Configuration
public class UserBeanConfiguration {

    @Bean
    public UserFacade userFacade(UserRepository userRepository) {
        return new UserService(userRepository);
    }
}