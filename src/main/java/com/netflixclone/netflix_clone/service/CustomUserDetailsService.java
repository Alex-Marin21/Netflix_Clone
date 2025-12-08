package com.netflixclone.netflix_clone.service;

import com.netflixclone.netflix_clone.entity.User;
import com.netflixclone.netflix_clone.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Custom implementation of Spring Security's UserDetailsService interface.
 * Serves as the bridge between the database (UserRepository) and the Spring Security authentication provider.
 * Responsible for loading user-specific data during the login process.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the username.
     * Retrieves the user entity from the database and maps it to a Spring Security compatible UserDetails object.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never null).
     * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Retrieve the user from the database; throw exception if not found
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));

        // 2. Prepare the authority (role) string. Spring Security typically expects the "ROLE_" prefix.
        String roleName = "ROLE_" + user.getRole();

        // 3. Map the application User entity to Spring Security's UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                user.getEnabled(), // Account enabled status
                true, true, true, // Account non-expired, credentials non-expired, account non-locked
                Collections.singletonList(new SimpleGrantedAuthority(roleName)) // Set the granted authority
        );
    }
}