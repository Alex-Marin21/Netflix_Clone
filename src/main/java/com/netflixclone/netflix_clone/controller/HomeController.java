package com.netflixclone.netflix_clone.controller;

import com.netflixclone.netflix_clone.entity.Movie;
import com.netflixclone.netflix_clone.entity.User;
import com.netflixclone.netflix_clone.repository.MovieRepository;
import com.netflixclone.netflix_clone.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;

/**
 * Controller responsible for the main application landing page.
 * Handles displaying the movie catalog, search functionality, and genre filtering.
 */
@Controller
public class HomeController {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public HomeController(MovieRepository movieRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    /**
     * Displays the main home page featuring the entire catalog of movies.
     * Populates the model with popular movies and the user's favorite list for UI state.
     *
     * @param model     the UI model used to pass data to the view
     * @param principal the currently authenticated user (used to fetch favorites)
     * @return the name of the home view template
     */
    @GetMapping("/")
    public String home(Model model, Principal principal) {
        List<Movie> movies = movieRepository.findAll();
        model.addAttribute("movies", movies);
        model.addAttribute("pageTitle", "Popular on Netflix");
        addFavoritesToModel(model, principal);
        return "home";
    }

    /**
     * Handles search requests by filtering movies based on the provided query string.
     * Reuses the home view to display search results.
     *
     * @param query     the search keyword provided by the user
     * @param model     the UI model used to pass data to the view
     * @param principal the currently authenticated user
     * @return the name of the home view template with filtered results
     */
    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model, Principal principal) {
        // Search the database for titles containing the query string (case-insensitive)
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(query);

        model.addAttribute("movies", movies);
        model.addAttribute("pageTitle", "Search Results for: '" + query + "'");
        model.addAttribute("lastQuery", query);

        addFavoritesToModel(model, principal);

        return "home";
    }

    /**
     * Filters the movie catalog based on a specific genre category.
     * Displays the subset of movies matching the requested genre.
     *
     * @param genreName the name of the genre to filter by (e.g., "Action", "Drama")
     * @param model     the UI model used to pass data to the view
     * @param principal the currently authenticated user
     * @return the name of the home view template with filtered results
     */
    @GetMapping("/genre/{genreName}")
    public String filterByGenre(@PathVariable String genreName, Model model, Principal principal) {
        // Retrieve only movies that contain the specified genre tag
        List<Movie> movies = movieRepository.findByGenresContaining(genreName);

        model.addAttribute("movies", movies);
        model.addAttribute("pageTitle", "Genre: " + genreName);

        addFavoritesToModel(model, principal);

        return "home";
    }

    /**
     * Helper method to inject the authenticated user's favorite movies into the model.
     * This allows the UI to correctly render 'Add to List' vs 'Remove from List' buttons
     * by checking if a displayed movie is already in the user's favorites.
     *
     * @param model     the UI model to update
     * @param principal the currently authenticated user
     */
    private void addFavoritesToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                model.addAttribute("favorites", user.getFavoriteMovies());
            }
        } else {
            // If the user is not logged in, pass an empty set to avoid null pointer exceptions in the view
            model.addAttribute("favorites", new HashSet<>());
        }
    }
}