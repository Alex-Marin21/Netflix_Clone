package com.netflixclone.netflix_clone.controller;

import com.netflixclone.netflix_clone.dto.UserRegistrationDto;
import com.netflixclone.netflix_clone.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller responsible for handling user authentication and registration workflows.
 * Manages the registration form, data submission, and account verification processes.
 */
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the user registration form.
     * Initializes an empty DTO to capture user input.
     *
     * @param model the UI model to hold form data
     * @return the name of the registration view template
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Initialize a blank DTO for the form binding
        UserRegistrationDto userDto = new UserRegistrationDto();
        model.addAttribute("user", userDto);
        return "register";
    }

    /**
     * Displays the account verification page where users enter their OTP code.
     *
     * @param email the email address of the user to verify
     * @param model the UI model to pass the email to the view
     * @return the name of the verification view template
     */
    @GetMapping("/verify-account")
    public String showVerificationPage(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "verify_account";
    }

    /**
     * Processes the account verification request by validating the provided code.
     *
     * @param email the user's email address
     * @param code  the verification code entered by the user
     * @param model the UI model for returning error messages if necessary
     * @return a redirect to login on success, or the verification view on failure
     */
    @PostMapping("/verify-account")
    public String verifyAccount(@RequestParam("email") String email,
                                @RequestParam("code") String code,
                                Model model) {

        boolean verified = userService.verifyAccount(email, code);

        if (verified) {
            return "redirect:/login?verified";
        } else {
            model.addAttribute("error", "Invalid or expired code");
            model.addAttribute("email", email);
            return "verify_account";
        }
    }

    /**
     * Handles the submission of the user registration form.
     * Validates input data, registers the user, and initiates the verification process.
     *
     * @param userDto the data transfer object containing form input
     * @param result  binding result for validation errors
     * @param model   the UI model for error display
     * @return a redirect to the verification page on success, or the registration view on error
     */
    @PostMapping("/register")
    public String registerUserAccount(@Valid @ModelAttribute("user") UserRegistrationDto userDto,
                                      BindingResult result,
                                      Model model) {
        // 1. Check for standard validation errors (e.g., email format, password length)
        if (result.hasErrors()) {
            return "register";
        }

        // 2. Attempt to save the new user account
        try {
            userService.save(userDto);
        } catch (RuntimeException e) {
            // Capture and display business logic errors (e.g., "User already exists")
            model.addAttribute("error", e.getMessage());
            return "register";
        }

        // 3. Redirect to account verification upon successful registration
        return "redirect:/verify-account?email=" + userDto.getEmail();
    }
}