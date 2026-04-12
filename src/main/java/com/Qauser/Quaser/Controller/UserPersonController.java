package com.Qauser.Quaser.Controller;

import com.Qauser.Quaser.Config.JwtService;
import com.Qauser.Quaser.Entity.User;
import com.Qauser.Quaser.Service.UserPersonService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(Map.of(
                "id", savedUser.getId(),
                "username", savedUser.getUsername(),
                "message", "User registered successfully"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        User authenticatedUser = userService.authenticate(user.getUsername(), user.getPassword());

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        String token = jwtService.generateToken(authenticatedUser);

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Login successful"));
    }

    @GetMapping("/isLoggedIn")
    public ResponseEntity<?> checkStatus(@AuthenticationPrincipal User user) {
        // 1. Handle Case: User not found in SecurityContext (Filter failed or no cookie)
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "unauthenticated", "message", "No active session"));
        }

        try {
            // 2. Safely extract role name to prevent NPE if role is null in DB
            String roleName = (user.getRole() != null) ? user.getRole().name() : "GUEST";

            // 3. Handle potential nulls for email or username fields
            String email = (user.getEmail() != null) ? user.getEmail() : "No Email";
            String username = (user.getUsername() != null) ? user.getUsername() : "Anonymous";

            return ResponseEntity.ok(Map.of(
                    "email", email,
                    "username", username,
                    "role", roleName,
                    "status", "authenticated"
            ));
        } catch (Exception e) {
            // 4. Global catch for this method to ensure the frontend gets a response
            // instead of a "hanging" request or raw 500 error.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Internal processing error"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Logged out successfully"));
    }
}