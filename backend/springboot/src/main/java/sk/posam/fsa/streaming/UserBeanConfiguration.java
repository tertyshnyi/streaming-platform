package sk.posam.fsa.streaming;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import sk.posam.fsa.streaming.domain.repositories.KeycloakRegistration;
import sk.posam.fsa.streaming.domain.repositories.UserRepository;
import sk.posam.fsa.streaming.domain.services.UserFacade;
import sk.posam.fsa.streaming.domain.services.UserService;
import sk.posam.fsa.streaming.jpa.KeycloakClient;
import sk.posam.fsa.streaming.jpa.KeycloakRegistrationAdapter;

@Configuration
public class UserBeanConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public KeycloakClient keycloakClient(
            RestTemplate restTemplate,
            @Value("${keycloak.url}") String keycloakUrl,
            @Value("${keycloak.realm}") String realm,
            @Value("${keycloak.client-id}") String clientId,
            @Value("${keycloak.client-secret}") String clientSecret
    ) {
        return new KeycloakClient(restTemplate, keycloakUrl, realm, clientId, clientSecret);
    }

    @Bean
    public KeycloakRegistration keycloakRegistration(KeycloakClient client) {
        return new KeycloakRegistrationAdapter(client);
    }

    @Bean
    public UserFacade userFacade(UserRepository userRepository, KeycloakRegistration keycloakRegistration) {
        return new UserService(userRepository, keycloakRegistration);
    }
}
