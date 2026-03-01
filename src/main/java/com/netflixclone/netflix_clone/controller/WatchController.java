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

/** Clasa pentru gestionarea detaliilor filmelor si a vizionarii
 * @author Marin-Sirbu Alex-Florin
 * @version 10 Ianuarie 2026
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

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable Integer id, Model model, Principal principal) {
        Movie movie = movieRepository.findById(id).orElseThrow();
        model.addAttribute("movie", movie);

        boolean isFavorite = false;
        if (principal != null) {
            String username = principal.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                isFavorite = user.getFavoriteMovies().contains(movie);
            }
        }
        model.addAttribute("isFavorite", isFavorite);

        String searchGenre = "";

        if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
            String[] genres = movie.getGenres().split(",");
            if (genres.length > 1) {
                searchGenre = genres[1].trim();
            } else {
                searchGenre = genres[0].trim();
            }
        }

        if (searchGenre.isEmpty()) searchGenre = "Drama";

        List<Movie> similarMovies = movieRepository.findSimilarRandom(searchGenre, movie.getMovieId());

        model.addAttribute("similarMovies", similarMovies);

        return "details";
    }

    @GetMapping("/watch/{id}")
    public String watchMovie(@PathVariable Integer id, Model model) {
        Movie movie = movieRepository.findById(id).orElseThrow();

        if (movie.getTrailerUrl() == null || movie.getTrailerUrl().isEmpty()) {
            System.out.println("No trailer found for: " + movie.getTitle() + ". Searching on YouTube...");

            String youtubeLink = youTubeService.searchTrailer(movie.getTitle());

            if (youtubeLink != null) {
                movie.setTrailerUrl(youtubeLink);
                movieRepository.save(movie);
                System.out.println("Trailer found and saved!");
            }
        }

        model.addAttribute("movie", movie);
        return "watch";
    }
}