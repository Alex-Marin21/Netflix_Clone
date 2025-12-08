package com.netflixclone.netflix_clone.controller;

import com.netflixclone.netflix_clone.entity.User;
import com.netflixclone.netflix_clone.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller responsible for administrative operations.
 * Manages the admin dashboard and user account administration.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the list of all registered users and displays the admin dashboard.
     *
     * @param model the UI model used to pass data to the view
     * @return the name of the Thymeleaf template for the admin dashboard
     */
    @GetMapping
    public String adminDashboard(Model model) {
        List<User> allUsers = userRepository.findAll();
        model.addAttribute("users", allUsers);
        return "admin_dashboard";
    }

    /**
     * Permanently deletes a user account from the system based on their ID.
     *
     * @param id the unique identifier of the user to be deleted
     * @return a redirect string to the admin dashboard with a success parameter
     */
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
        return "redirect:/admin?deleted";
    }
}