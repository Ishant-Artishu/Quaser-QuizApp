package com.Qauser.Quaser.Service;

import com.Qauser.Quaser.Entity.Role; // Make sure to import your Role enum
import com.Qauser.Quaser.Entity.User;
import com.Qauser.Quaser.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserPersonService implements UserDetailsService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        // 1. Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 2. CRITICAL FIX: Assign a default role if none is provided
        // This prevents the NullPointerException you saw in the logs
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        return userRepository.save(user);
    }

    public User authenticate(String email, String password) {
        // Using findByEmail since we are on an email-based system
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}