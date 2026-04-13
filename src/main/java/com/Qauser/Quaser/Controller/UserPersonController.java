package com.Qauser.Quaser.Controller;

import com.Qauser.Quaser.Entity.User;
import com.Qauser.Quaser.Entity.Role;
import com.Qauser.Quaser.Service.UserPersonService;
import com.Qauser.Quaser.Config.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/userPeople")
public class UserPersonController {

    private final UserPersonService userService;
    private final JwtService jwtService;

    public UserPersonController(UserPersonService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // 1. LOGIN: Returns token in JSON body
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        // Authenticate using email and password
        User authenticatedUser = userService.authenticate(user.getEmail(), user.getPassword());

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "false", "error", "Invalid Credentials"));
        }

        // Generate the JWT
        String token = jwtService.generateToken(authenticatedUser);

        // Return token directly to frontend
        return ResponseEntity.ok(Map.of(
                "message", "true",
                "token", token,
                "username", authenticatedUser.getUsername(),
                "role", (authenticatedUser.getRole() != null ? authenticatedUser.getRole().name() : "USER")
        ));
    }

    // 2. REGISTER: Aligned with your Service logic
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        if (userService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "false", "error", "User already exists"));
        }

        // Safety: Ensure role is not null to prevent Filter crashes
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "true", "details", "Registration Successful"));
    }

    // 3. STATUS: Validated by JwtAuthFilter
    @GetMapping("/isLoggedIn")
    public ResponseEntity<Map<String, String>> checkStatus(@AuthenticationPrincipal UserDetails userDetails) {
        // If JwtAuthFilter fails to find or validate the Bearer token, userDetails will be null
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "Not Valid", "authenticated", "false"));
        }

        return ResponseEntity.ok(Map.of(
                "username", userDetails.getUsername(),
                "status", "authenticated",
                "authenticated", "true"
        ));
    }
}