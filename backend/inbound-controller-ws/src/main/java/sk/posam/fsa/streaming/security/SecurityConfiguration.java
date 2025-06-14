package sk.posam.fsa.streaming.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class SecurityConfiguration implements WebMvcConfigurer {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(this::configureAuthorizationRules)
                .oauth2ResourceServer(oauth2 -> configureOauth2ResourceServer(oauth2, jwtDecoder))
                .build();
    }

    private void configureAuthorizationRules(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/**").permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()

                .requestMatchers(HttpMethod.POST, "/movies/**").hasAnyRole("ADMIN", "RELEASER")
                .requestMatchers(HttpMethod.PUT, "/movies/**").hasAnyRole("ADMIN", "RELEASER")
                .requestMatchers(HttpMethod.DELETE, "/movies/**").hasAnyRole("ADMIN", "RELEASER")

                .requestMatchers(HttpMethod.POST, "/series/**").hasAnyRole("ADMIN", "RELEASER")
                .requestMatchers(HttpMethod.PUT, "/series/**").hasAnyRole("ADMIN", "RELEASER")
                .requestMatchers(HttpMethod.DELETE, "/series/**").hasAnyRole("ADMIN", "RELEASER")

                .anyRequest().permitAll();
    }

    private void configureOauth2ResourceServer(OAuth2ResourceServerConfigurer<HttpSecurity> oauth2, JwtDecoder jwtDecoder) {
        oauth2
                .jwt(jwt -> {
                    jwt.decoder(jwtDecoder);
                    jwt.jwtAuthenticationConverter(JwtConverter::new);
                });
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://20.166.32.72.nip.io",
                        "http://localhost:4200"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
