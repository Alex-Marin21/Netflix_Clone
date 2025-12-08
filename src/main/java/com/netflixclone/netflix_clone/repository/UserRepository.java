package com.netflixclone.netflix_clone.repository;

import com.netflixclone.netflix_clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data Access Object for the User entity.
 * Leverages Spring Data JPA to automatically generate SQL queries for standard operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Retrieves a user entity by its email address.
     * Spring Data automatically generates the SQL: SELECT * FROM users WHERE email = ?
     *
     * @param email the email address to search for
     * @return an Optional containing the user if found, or empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves a user entity by its unique username.
     * Primarily used by Spring Security during the authentication process.
     *
     * @param username the username to search for
     * @return an Optional containing the user if found, or empty otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user already exists with the provided email address.
     * Used during registration validation to prevent duplicate accounts.
     *
     * @param email the email address to check
     * @return true if the email is already registered, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a user already exists with the provided username.
     * Used during registration validation to ensure username uniqueness.
     *
     * @param username the username to check
     * @return true if the username is already taken, false otherwise
     */
    boolean existsByUsername(String username);
}