package com.netflixclone.netflix_clone.service;

import com.netflixclone.netflix_clone.dto.UserRegistrationDto;
import com.netflixclone.netflix_clone.entity.User;
import com.netflixclone.netflix_clone.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Random;

/**
 * Implementation of the UserService interface.
 * Handles the core business logic for user management, including registration,
 * authentication support, and account verification via email.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Generates a 6-digit random code for password recovery and emails it to the user.
     *
     * @param email the email address requesting the password reset
     * @throws RuntimeException if the email is not found in the database
     */
    @Override
    public void generateResetCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email address not found in the database."));

        // 1. Generate a random 6-digit code
        String code = String.valueOf(new Random().nextInt(900000) + 100000);

        // 2. Save the code to the user's record
        user.setVerificationCode(code);
        userRepository.save(user);

        // 3. Send the email
        emailService.sendEmail(email,
                "Netflix Clone Password Reset Code",
                "Hello! Your verification code is: " + code);
    }

    /**
     * Verifies if the provided reset code matches the one stored in the database.
     *
     * @param email the user's email address
     * @param code  the code provided by the user
     * @return true if the code matches, false otherwise
     */
    @Override
    public boolean verifyResetCode(String email, String code) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || user.getVerificationCode() == null) {
            return false;
        }
        // Check if the stored code matches the provided one
        return user.getVerificationCode().equals(code);
    }

    /**
     * Updates the user's password after successful verification.
     * Clears the verification code to prevent reuse.
     *
     * @param email       the user's email address
     * @param newPassword the new password to set
     */
    @Override
    @Transactional
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Encrypt and set the new password
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        // Clear the verification code
        user.setVerificationCode(null);

        userRepository.save(user);
    }

    /**
     * Registers a new user.
     * Checks for duplicate emails/usernames, encrypts the password, sets the account to disabled,
     * and sends an activation code via email.
     *
     * @param registrationDto the user registration data
     * @throws RuntimeException if the email or username is already taken
     */
    @Override
    @Transactional
    public void save(UserRegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("An account with this email already exists!");
        }
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("This username is already taken!");
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));

        // --- VERIFICATION LOGIC ---

        // 1. Set account as INACTIVE (cannot login until verified)
        user.setEnabled(false);

        // 2. Generate activation code (6 digits)
        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        user.setVerificationCode(code);

        userRepository.save(user);

        // 3. Send the welcome/activation email
        emailService.sendEmail(user.getEmail(),
                "Activate your Netflix Clone Account",
                "Welcome! Your activation code is: " + code);
    }

    /**
     * Verifies the account activation code.
     * If correct, enables the user account.
     *
     * @param email the user's email
     * @param code  the activation code
     * @return true if successful, false otherwise
     */
    @Override
    public boolean verifyAccount(String email, String code) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && user.getVerificationCode() != null && user.getVerificationCode().equals(code)) {
            // Code is correct -> Activate account
            user.setEnabled(true);
            user.setVerificationCode(null); // Clear the code
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}