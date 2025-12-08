package com.netflixclone.netflix_clone.controller;

import com.netflixclone.netflix_clone.entity.Movie;
import com.netflixclone.netflix_clone.entity.User;
import com.netflixclone.netflix_clone.repository.MovieRepository;
import com.netflixclone.netflix_clone.repository.UserRepository;
import com.netflixclone.netflix_clone.service.YouTubeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.security.Principal;

/**
 * Controller responsible for movie details and streaming functionality.
 * Handles the logic for displaying movie information, the recommendation engine,
 * and the "lazy loading" of video trailers via the YouTube API.
 */
@Controller
public class WatchController {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final YouTubeService youTubeService;

    public WatchController(MovieRepository movieRepository, UserRepository userRepository, YouTubeService youTubeService) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.youTubeService = youTubeService;
    }

    /**
     * Displays the detailed information page for a specific movie.
     * Includes logic for checking if the movie is in the user's favorites
     * and generates a list of recommended movies based on genre.
     *
     * @param id        the unique identifier of the movie
     * @param model     the UI model used to pass data to the view
     * @param principal the currently authenticated user
     * @return the name of the details view template
     */
    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable Integer id, Model model, Principal principal) {
        Movie movie = movieRepository.findById(id).orElseThrow();
        model.addAttribute("movie", movie);

        // 1. Check if the movie is in the user's "My List" (Favorites)
        boolean isFavorite = false;
        if (principal != null) {
            String username = principal.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                isFavorite = user.getFavoriteMovies().contains(movie);
            }
        }
        model.addAttribute("isFavorite", isFavorite);

        // 2. Recommendation Engine Logic
        // Selects similar movies based on the secondary genre for better accuracy.
        String searchGenre = "";

        if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
            String[] genres = movie.getGenres().split(",");
            // Heuristic: If multiple genres exist, pick the second one (often more specific).
            // Example: From "Drama,Horror", use "Horror" instead of the generic "Drama".
            if (genres.length > 1) {
                searchGenre = genres[1].trim();
            } else {
                searchGenre = genres[0].trim();
            }
        }

        // Fallback to "Drama" if no genre data is found
        if (searchGenre.isEmpty()) searchGenre = "Drama";

        // Fetch random similar movies excluding the current one
        List<Movie> similarMovies = movieRepository.findSimilarRandom(searchGenre, movie.getMovieId());

        model.addAttribute("similarMovies", similarMovies);

        return "details";
    }

    /**
     * Handles the video streaming page.
     * Implements "Lazy Loading" for trailers: if a trailer URL is missing,
     * it automatically searches for it via the YouTube API and saves it to the database.
     *
     * @param id    the unique identifier of the movie to watch
     * @param model the UI model used to pass the movie data to the view
     * @return the name of the watch view template
     */
    @GetMapping("/watch/{id}")
    public String watchMovie(@PathVariable Integer id, Model model) {
        Movie movie = movieRepository.findById(id).orElseThrow();

        // Lazy Loading Logic for YouTube Trailers
        // If the trailer URL is missing in the DB, fetch it on-demand
        if (movie.getTrailerUrl() == null || movie.getTrailerUrl().isEmpty()) {
            System.out.println("No trailer found for: " + movie.getTitle() + ". Searching on YouTube...");

            String youtubeLink = youTubeService.searchTrailer(movie.getTitle());

            if (youtubeLink != null) {
                movie.setTrailerUrl(youtubeLink);
                movieRepository.save(movie); // Cache the link in the DB for future use
                System.out.println("Trailer found and saved!");
            }
        }

        model.addAttribute("movie", movie);
        return "watch";
    }
}