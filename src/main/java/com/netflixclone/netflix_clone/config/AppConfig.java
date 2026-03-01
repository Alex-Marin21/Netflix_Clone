package com.netflixclone.netflix_clone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/** Clasa pentru configurarea bean-urilor globale ale aplicatiei
 * @author Marin-Sirbu Alex-Florin
 * @version 10 Ianuarie 2026
 */
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}