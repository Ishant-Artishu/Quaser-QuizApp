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

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        try {
            // matches your Optional-based service
            if (userService.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "false", "error", "User already exists"));
            }

            if (user.getRole() == null) {
                user.setRole(Role.USER);
            }

            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "true", "details", "User registered successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "false", "error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        User authenticatedUser = userService.authenticate(user.getUsername(), user.getPassword());

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "false", "error", "Invalid Credentials"));
        }

        String token = jwtService.generateToken(authenticatedUser);

        return ResponseEntity.ok(Map.of(
                "message", "true",
                "token", token,
                "username", authenticatedUser.getUsername(),
                "role", (authenticatedUser.getRole() != null ? authenticatedUser.getRole().name() : "USER")
        ));
    }

    @GetMapping("/isLoggedIn")
    public ResponseEntity<Map<String, String>> checkStatus(@AuthenticationPrincipal UserDetails userDetails) {
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