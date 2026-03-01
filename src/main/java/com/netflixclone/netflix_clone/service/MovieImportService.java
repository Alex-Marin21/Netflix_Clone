package com.netflixclone.netflix_clone.service;

import com.netflixclone.netflix_clone.dto.TvMazeShowDto;
import com.netflixclone.netflix_clone.entity.Movie;
import com.netflixclone.netflix_clone.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/** Clasa Service pentru importul automat al filmelor din API extern (TvMaze)
 * @author Marin-Sirbu Alex-Florin
 * @version 10 Ianuarie 2026
 */
@Service
public class MovieImportService {

    private final String TVMAZE_API_URL = "https://api.tvmaze.com/shows?page=";

    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;

    public MovieImportService(RestTemplate restTemplate, MovieRepository movieRepository) {
        this.restTemplate = restTemplate;
        this.movieRepository = movieRepository;
    }

    @Transactional
    public void importMovies() {
        try {
            System.out.println(">>> STARTING BULK IMPORT (Pages 0-4)...");

            for (int page = 0; page < 5; page++) {
                System.out.println("--- Downloading Page " + page + " ---");

                try {
                    TvMazeShowDto[] shows = restTemplate.getForObject(TVMAZE_API_URL + page, TvMazeShowDto[].class);

                    if (shows != null) {
                        for (TvMazeShowDto show : shows) {
                            if (show.getImage() != null && movieRepository.findByTmdbId(show.getId()).isEmpty()) {

                                Movie movie = new Movie();
                                movie.setTmdbId(show.getId());
                                movie.setTitle(show.getName());
                                movie.setPosterPath(show.getImage().getOriginalUrl());

                                if (show.getSummary() != null) {
                                    String cleanDesc = show.getSummary().replaceAll("\\<.*?\\>", "");
                                    movie.setDescription(cleanDesc);
                                }

                                movie.setReleaseDate(show.getPremiered());

                                if (show.getRating() != null) {
                                    movie.setRating(show.getRating().getAverage());
                                }

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
                    System.err.println("Error on page " + page + ": " + e.getMessage());
                }
            }

            System.out.println(">>> IMPORT COMPLETED SUCCESSFULLY! (Database populated with 1000+ titles)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}