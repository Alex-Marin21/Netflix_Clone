package com.netflixclone.netflix_clone.controller;

import com.netflixclone.netflix_clone.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller responsible for the password recovery workflow.
 * Handles the multi-step process: email submission, OTP verification, and password reset.
 */
@Controller
public class ForgotPasswordController {

    private final UserService userService;

    public ForgotPasswordController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the initial form where the user enters their email address.
     * Corresponds to Step 1 of the recovery process.
     *
     * @return the name of the forgot password view template
     */
    @GetMapping("/forgot-password")
    public String showEmailForm() {
        return "forgot_password";
    }

    /**
     * Processes the submitted email address.
     * Generates a reset code if the user exists and directs them to the verification page.
     *
     * @param email the email address entered by the user
     * @param model the UI model used to pass data (email/errors) to the view
     * @return the verification view on success, or the form view on error
     */
    @PostMapping("/forgot-password")
    public String processEmailForm(@RequestParam("email") String email, Model model) {
        try {
            userService.generateResetCode(email);
            // Pass the email to the next view to associate the verification code with the correct user
            model.addAttribute("email", email);
            return "verify_code";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "forgot_password";
        }
    }

    /**
     * Verifies the OTP code provided by the user.
     * Corresponds to Step 2 of the recovery process.
     *
     * @param email the user's email address
     * @param code  the verification code entered by the user
     * @param model the UI model for error handling
     * @return the password reset view on success, or the verification view on failure
     */
    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam("email") String email,
                             @RequestParam("code") String code,
                             Model model) {
        if (userService.verifyResetCode(email, code)) {
            model.addAttribute("email", email);
            return "reset_password";
        } else {
            model.addAttribute("error", "Invalid or expired code!");
            model.addAttribute("email", email);
            return "verify_code";
        }
    }

    /**
     * Updates the user's password in the database.
     * Corresponds to Step 3 of the recovery process.
     *
     * @param email    the user's email address
     * @param password the new password to be set
     * @return a redirect to the login page with a success parameter
     */
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("password") String password) {
        userService.updatePassword(email, password);
        return "redirect:/login?resetSuccess";
    }
}