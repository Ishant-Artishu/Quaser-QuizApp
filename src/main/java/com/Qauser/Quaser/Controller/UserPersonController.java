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
        // Note: Using findByEmail logic in service, but mapped to user.getUsername() if that's where email is stored
        User authenticatedUser = userService.authenticate(user.getUsername(), user.getPassword());

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        String token = jwtService.generateToken(authenticatedUser);

        // Build Cookie for Railway (HTTPS) to Localhost (HTTP/HTTPS)
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true)    // Required for SameSite=None
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("None") // Required for Cross-Origin
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Login successful"));
    }

    /**
     * Changed to GET to avoid 403 Forbidden issues common with POST status checks.
     * Ensure React calls: axios.get(".../isLoggedIn", { withCredentials: true })
     */
    @GetMapping("/isLoggedIn")
    public ResponseEntity<?> checkStatus(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "unauthenticated"));
        }

        // Return user info so frontend can update the UI/Navbar
        return ResponseEntity.ok(Map.of(
                "email", user.getEmail(),
                "username", user.getUsername(),
                "role", user.getRole(),
                "status", "authenticated"
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Clear the cookie by setting maxAge to 0
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