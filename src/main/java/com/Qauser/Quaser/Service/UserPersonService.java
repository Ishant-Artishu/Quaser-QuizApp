package com.Qauser.Quaser.Service;

import com.Qauser.Quaser.Entity.Role;
import com.Qauser.Quaser.Entity.User;
import com.Qauser.Quaser.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPersonService implements UserDetailsService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Finds a user by email.
     * Required by the Controller to check for duplicates during registration.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Encodes password and saves new user to the database.
     */
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        return userRepository.save(user);
    }

    /**
     * Authenticates user for login.
     */
    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            if (user.getRole() == null) user.setRole(Role.USER);
            return user;
        }
        return null;
    }

    /**
     * Required by Spring Security's UserDetailsService.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + email));

        // Safety check to prevent NullPointerException in the Filter
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        return user;
    }
}