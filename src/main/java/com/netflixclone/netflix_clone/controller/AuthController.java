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

/** Clasa pentru gestionarea autentificarii si inregistrarii utilizatorilor
 * @author Marin-Sirbu Alex-Florin
 * @version 10 Ianuarie 2026
 */
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserRegistrationDto userDto = new UserRegistrationDto();
        model.addAttribute("user", userDto);
        return "register";
    }

    @GetMapping("/verify-account")
    public String showVerificationPage(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "verify_account";
    }

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

    @PostMapping("/register")
    public String registerUserAccount(@Valid @ModelAttribute("user") UserRegistrationDto userDto,
                                      BindingResult result,
                                      Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.save(userDto);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }

        return "redirect:/verify-account?email=" + userDto.getEmail();
    }
}