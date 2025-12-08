package com.netflixclone.netflix_clone.service;

import com.netflixclone.netflix_clone.dto.UserRegistrationDto;
import com.netflixclone.netflix_clone.entity.User;

/**
 * Service interface defining the contract for user management operations.
 * Handles the business logic for user registration, authentication support,
 * account verification, and password recovery.
 */
public interface UserService {

    /**
     * Registers a new user in the system.
     * Validates the registration data (DTO), encrypts the password, and persists the user entity.
     *
     * @param registrationDto the data transfer object containing user registration details
     */
    void save(UserRegistrationDto registrationDto);

    /**
     * Retrieves a user entity based on their unique username.
     * Primarily used during the authentication process to load user details.
     *
     * @param username the username to search for
     * @return the User entity if found, or null otherwise
     */
    User findByUsername(String username);

    /**
     * Generates a password reset code (OTP) for the specified email address.
     * The code is typically sent to the user via email.
     *
     * @param email the email address associated with the account to recover
     */
    void generateResetCode(String email);

    /**
     * Verifies the validity of a password reset code for a given email.
     * Checks if the code matches the one stored in the database and if it has not expired.
     *
     * @param email the email address of the user
     * @param code  the reset code provided by the user
     * @return true if the code is valid, false otherwise
     */
    boolean verifyResetCode(String email, String code);

    /**
     * Updates the password for the user associated with the given email address.
     * This method is called after a successful password reset verification.
     *
     * @param email       the email address of the user
     * @param newPassword the new password to be set (will be encrypted)
     */
    void updatePassword(String email, String newPassword);

    /**
     * Verifies a new account using the activation code sent during registration.
     * Enables the user account if the verification is successful.
     *
     * @param email the email address of the new user
     * @param code  the activation code provided by the user
     * @return true if the account was successfully verified and enabled, false otherwise
     */
    boolean verifyAccount(String email, String code);
}