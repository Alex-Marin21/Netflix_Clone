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

/** Clasa pentru gestionarea paginii principale si a filtrarii filmelor
 * @author Marin-Sirbu Alex-Florin
 * @version 10 Ianuarie 2026
 */
@Controller
public class HomeController {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public HomeController(MovieRepository movieRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        List<Movie> movies = movieRepository.findAll();
        model.addAttribute("movies", movies);
        model.addAttribute("pageTitle", "Popular on Netflix");
        addFavoritesToModel(model, principal);
        return "home";
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model, Principal principal) {
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(query);

        model.addAttribute("movies", movies);
        model.addAttribute("pageTitle", "Search Results for: '" + query + "'");
        model.addAttribute("lastQuery", query);

        addFavoritesToModel(model, principal);

        return "home";
    }

    @GetMapping("/genre/{genreName}")
    public String filterByGenre(@PathVariable String genreName, Model model, Principal principal) {
        List<Movie> movies = movieRepository.findByGenresContaining(genreName);

        model.addAttribute("movies", movies);
        model.addAttribute("pageTitle", "Genre: " + genreName);

        addFavoritesToModel(model, principal);

        return "home";
    }

    private void addFavoritesToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                model.addAttribute("favorites", user.getFavoriteMovies());
            }
        } else {
            model.addAttribute("favorites", new HashSet<>());
        }
    }
}