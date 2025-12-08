package com.netflixclone.netflix_clone.service;

import com.netflixclone.netflix_clone.dto.TvMazeShowDto;
import com.netflixclone.netflix_clone.entity.Movie;
import com.netflixclone.netflix_clone.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Service responsible for populating the movie database from external sources.
 * Integrates with the TVMaze API to fetch, parse, and save movie/show metadata.
 */
@Service
public class MovieImportService {

    // Base URL for the TVMaze Show API (pagination parameter to be appended)
    private final String TVMAZE_API_URL = "https://api.tvmaze.com/shows?page=";

    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;

    public MovieImportService(RestTemplate restTemplate, MovieRepository movieRepository) {
        this.restTemplate = restTemplate;
        this.movieRepository = movieRepository;
    }

    /**
     * Executes the bulk import process.
     * Iterates through multiple pages of the external API to fetch a large dataset (approx. 1000+ titles).
     * This operation is transactional to ensure data integrity during the batch save.
     */
    @Transactional
    public void importMovies() {
        try {
            System.out.println(">>> STARTING BULK IMPORT (Pages 0-4)...");

            // Loop through the first 5 pages of the API (Page 0 to 4).
            // Each page typically contains ~250 items, resulting in over 1000 titles total.
            for (int page = 0; page < 5; page++) {

                System.out.println("--- Downloading Page " + page + " ---");

                try {
                    // Fetch the specific page: https://api.tvmaze.com/shows?page=X
                    TvMazeShowDto[] shows = restTemplate.getForObject(TVMAZE_API_URL + page, TvMazeShowDto[].class);

                    if (shows != null) {
                        for (TvMazeShowDto show : shows) {
                            // Validation: Ensure the show has a poster image and does not already exist in the DB
                            if (show.getImage() != null && movieRepository.findByTmdbId(show.getId()).isEmpty()) {

                                Movie movie = new Movie();
                                movie.setTmdbId(show.getId());
                                movie.setTitle(show.getName());
                                movie.setPosterPath(show.getImage().getOriginalUrl());

                                // Clean HTML tags (like <p> or <b>) from the description summary
                                if (show.getSummary() != null) {
                                    String cleanDesc = show.getSummary().replaceAll("\\<.*?\\>", "");
                                    movie.setDescription(cleanDesc);
                                }

                                movie.setReleaseDate(show.getPremiered());

                                if (show.getRating() != null) {
                                    movie.setRating(show.getRating().getAverage());
                                }

                                // Map Genres: Join the array into a single comma-separated string
                                if (show.getGenres() != null && !show.getGenres().isEmpty()) {
                                    movie.setGenres(String.join(",", show.getGenres()));
                                } else {
                                    movie.setGenres("General");
                                }

                                movieRepository.save(movie);
                            }
                        }
                    }
                } catch (Exception e) {
                    // Log error for the specific page but continue the loop for subsequent pages
                    System.err.println("Error on page " + page + ": " + e.getMessage());
                }
            }

            System.out.println(">>> IMPORT COMPLETED SUCCESSFULLY! (Database populated with 1000+ titles)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}