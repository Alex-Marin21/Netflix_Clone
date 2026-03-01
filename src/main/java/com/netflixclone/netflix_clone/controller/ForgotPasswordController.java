package com.netflixclone.netflix_clone.controller;

import com.netflixclone.netflix_clone.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Clasa pentru gestionarea fluxului de recuperare a parolei
 * @author Marin-Sirbu Alex-Florin
 * @version 10 Ianuarie 2026
 */
@Controller
public class ForgotPasswordController {

    private final UserService userService;

    public ForgotPasswordController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/forgot-password")
    public String showEmailForm() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String processEmailForm(@RequestParam("email") String email, Model model) {
        try {
            userService.generateResetCode(email);
            model.addAttribute("email", email);
            return "verify_code";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "forgot_password";
        }
    }

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

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("password") String password) {
        userService.updatePassword(email, password);
        return "redirect:/login?resetSuccess";
    }
}