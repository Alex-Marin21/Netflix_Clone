package com.netflixclone.netflix_clone.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) used for user registration.
 * Captures and validates the input data sent from the sign-up form
 * before it is processed by the service layer.
 */
public class UserRegistrationDto {

    /**
     * The unique username for the new account.
     * Validation: Cannot be empty and must be between 3 and 50 characters.
     */
    @NotEmpty(message = "Username is required.")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    private String username;

    /**
     * The email address for the new account.
     * Validation: Cannot be empty and must follow standard email formatting.
     */
    @NotEmpty(message = "Email is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    /**
     * The password for the new account.
     * Validation: Cannot be empty and must be at least 6 characters long.
     */
    @NotEmpty(message = "Password is required.")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    private String password;

    // Default no-argument constructor
    public UserRegistrationDto() {}

    // Parameterized constructor for easier instantiation in tests or service calls
    public UserRegistrationDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}