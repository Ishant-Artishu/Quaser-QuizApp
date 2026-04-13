package com.Qauser.Quaser.Controller;

import com.Qauser.Quaser.Entity.User;
import com.Qauser.Quaser.Service.UserPersonService;
import com.Qauser.Quaser.Config.JwtService;
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

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        User authenticatedUser = userService.authenticate(user.getUsername(), user.getPassword());

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "false"));
        }

        String token = jwtService.generateToken(authenticatedUser);

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true) // Required for SameSite=None
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "true"));
    }

    @GetMapping("/isLoggedIn")
    public ResponseEntity<?> checkStatus(@AuthenticationPrincipal User user) {
        // user is populated by the filter, even if SecurityConfig is permitAll()
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Not Valid"));
        }

        // Handle case where user exists but role might be null in DB
        String roleName = (user.getRole() != null) ? user.getRole().name() : "USER";

        return ResponseEntity.ok(Map.of(
                "email", user.getEmail(),
                "username", user.getUsername(),
                "role", roleName,
                "status", "authenticated"
        ));
    }

    // ... rest of your controller (register/logout)
}