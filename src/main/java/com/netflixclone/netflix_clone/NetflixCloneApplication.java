package com.netflixclone.netflix_clone;

import com.netflixclone.netflix_clone.service.MovieImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The primary entry point for the Netflix Clone Spring Boot application.
 * Responsible for bootstrapping the application context and triggering initial data loading.
 */
@SpringBootApplication
public class NetflixCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetflixCloneApplication.class, args);
    }

    /**
     * Configures a CommandLineRunner that executes automatically upon application startup.
     * Triggers the {@link MovieImportService} to populate the database with movie data
     * from external sources (TVMaze API) immediately after the context is loaded.
     *
     * @param importService the service responsible for fetching and saving movie data
     * @return a CommandLineRunner instance that executes the import logic
     */
    @Bean
    public CommandLineRunner runImport(MovieImportService importService) {
        return args -> {
            importService.importMovies();
        };
    }
}