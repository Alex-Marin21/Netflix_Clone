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

/** Clasa pentru gestionarea listei de favorite a utilizatorului
 * @author Marin-Sirbu Alex-Florin
 * @version 10 Ianuarie 2026
 */
@Controller
public class MyListController {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public MyListController(UserRepository userRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/mylist")
    public String showMyList(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        model.addAttribute("movies", user.getFavoriteMovies());
        return "my_list";
    }

    @GetMapping("/mylist/add/{movieId}")
    public String addMovie(@PathVariable Integer movieId,
                           Principal principal,
                           @RequestHeader(value = "Referer", required = false) String referer) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Movie movie = movieRepository.findById(movieId).orElseThrow();

        user.addMovie(movie);
        userRepository.save(user);

        return "redirect:" + (referer != null ? referer : "/");
    }

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