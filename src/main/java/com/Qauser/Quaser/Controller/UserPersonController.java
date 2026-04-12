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

import java.util.HashMap;
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
        // Security best practice: Don't return the password (even if hashed) in the response
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
            // 401 is more accurate than 403 for failed login credentials
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }

        String token = jwtService.generateToken(authenticatedUser);

        // Build the Cookie - MUST use None and True for Cross-Site (Railway -> Localhost)
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)            // Protects against XSS
                .secure(true)              // Required for SameSite=None (Must be served over HTTPS)
                .path("/")                 // Makes cookie available for all endpoints (like /quiz)
                .maxAge(24 * 60 * 60)      // 1 day expiry
                .sameSite("None")          // Allows cookie to be sent from Railway to Localhost
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Login successful"));
    }

    @PostMapping("/isLoggedIn")
    public ResponseEntity<?> checkLoginStatus(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        // Return a small map of user info for the frontend
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", user.getEmail());
        userInfo.put("role", user.getRole());
        userInfo.put("username", user.getUsername());

        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // To logout, we overwrite the cookie with maxAge 0 to delete it
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