package com.netflixclone.netflix_clone.service;

import com.netflixclone.netflix_clone.dto.UserRegistrationDto;
import com.netflixclone.netflix_clone.entity.User;

/** Interfata Service pentru gestionarea utilizatorilor si a logicii de business
 * @author Marin-Sirbu Alex-Florin
 * @version 10 Ianuarie 2026
 */
public interface UserService {

    void save(UserRegistrationDto registrationDto);

    User findByUsername(String username);

    void generateResetCode(String email);

    boolean verifyResetCode(String email, String code);

    void updatePassword(String email, String newPassword);

    boolean verifyAccount(String email, String code);
}