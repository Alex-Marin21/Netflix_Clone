package com.netflixclone.netflix_clone.controller;

import com.netflixclone.netflix_clone.entity.Movie;
import com.netflixclone.netflix_clone.entity.User;
import com.netflixclone.netflix_clone.repository.MovieRepository;
import com.netflixclone.netflix_clone.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.security.Principal;

/**
 * Controller responsible for managing the user's personal "My List" of favorite movies.
 * Handles operations for viewing, adding, and removing movies from the favorites list.
 */
@Controller
public class MyListController {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public MyListController(UserRepository userRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    /**
     * Displays the "My List" page containing the current user's favorite movies.
     *
     * @param model     the UI model used to pass the list of movies to the view
     * @param principal the currently authenticated user
     * @return the name of the view template that renders the user's list
     */
    @GetMapping("/mylist")
    public String showMyList(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        model.addAttribute("movies", user.getFavoriteMovies());
        return "my_list";
    }

    /**
     * Adds a specific movie to the user's list of favorites.
     * Redirects the user back to the page they came from (Referer) to maintain a smooth UX.
     *
     * @param movieId   the ID of the movie to add
     * @param principal the currently authenticated user
     * @param referer   the URL of the previous page (used for redirection)
     * @return a redirect string to the previous page or the home page if the referer is missing
     */
    @GetMapping("/mylist/add/{movieId}")
    public String addMovie(@PathVariable Integer movieId,
                           Principal principal,
                           @RequestHeader(value = "Referer", required = false) String referer) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Movie movie = movieRepository.findById(movieId).orElseThrow();

        user.addMovie(movie);
        userRepository.save(user);

        // Redirect back to the originating page if available, otherwise default to home
        return "redirect:" + (referer != null ? referer : "/");
    }

    /**
     * Removes a specific movie from the user's list of favorites.
     * Redirects the user back to the page they came from (Referer).
     *
     * @param movieId   the ID of the movie to remove
     * @param principal the currently authenticated user
     * @param referer   the URL of the previous page
     * @return a redirect string to the previous page or the "My List" page if the referer is missing
     */
    @GetMapping("/mylist/remove/{movieId}")
    public String removeMovie(@PathVariable Integer movieId,
                              Principal principal,
                              @RequestHeader(value = "Referer", required = false) String referer) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Movie movie = movieRepository.findById(movieId).orElseThrow();

        user.removeMovie(movie);
        userRepository.save(user);

        return "redirect:" + (referer != null ? referer : "/mylist");
    }
}