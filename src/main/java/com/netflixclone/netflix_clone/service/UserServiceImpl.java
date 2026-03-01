package com.netflixclone.netflix_clone.service;

import com.netflixclone.netflix_clone.dto.UserRegistrationDto;
import com.netflixclone.netflix_clone.entity.User;
import com.netflixclone.netflix_clone.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Random;

/** Implementarea serviciului pentru gestionarea utilizatorilor
 * @author Marin-Sirbu Alex-Florin
 * @version 10 Ianuarie 2026
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

    @Override
    public void generateResetCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email address not found in the database."));

        String code = String.valueOf(new Random().nextInt(900000) + 100000);

        user.setVerificationCode(code);
        userRepository.save(user);

        emailService.sendEmail(email,
                "Netflix Clone Password Reset Code",
                "Hello! Your verification code is: " + code);
    }

    @Override
    public boolean verifyResetCode(String email, String code) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || user.getVerificationCode() == null) {
            return false;
        }
        return user.getVerificationCode().equals(code);
    }

    @Override
    @Transactional
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setVerificationCode(null);

        userRepository.save(user);
    }

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

        user.setEnabled(false);

        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        user.setVerificationCode(code);

        userRepository.save(user);

        emailService.sendEmail(user.getEmail(),
                "Activate your Netflix Clone Account",
                "Welcome! Your activation code is: " + code);
    }

    @Override
    public boolean verifyAccount(String email, String code) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && user.getVerificationCode() != null && user.getVerificationCode().equals(code)) {
            user.setEnabled(true);
            user.setVerificationCode(null);
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