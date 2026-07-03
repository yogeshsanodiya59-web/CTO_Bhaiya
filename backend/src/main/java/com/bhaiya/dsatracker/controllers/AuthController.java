package com.bhaiya.dsatracker.controllers;

import com.bhaiya.dsatracker.JwtService;
import com.bhaiya.dsatracker.models.User;
import com.bhaiya.dsatracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
        }

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));
        }

        String name = body.get("name");

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name != null && !name.trim().isEmpty() ? name : email.split("@")[0]);
        userRepository.save(user);

        String token = jwtService.generateToken(email);
        return ResponseEntity.ok(Map.of("token", token, "email", email, "name", user.getName(), "streak", user.getCurrentStreak() != null ? user.getCurrentStreak() : 0));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        String token = jwtService.generateToken(email);
        return ResponseEntity.ok(Map.of("token", token, "email", email, "name", userOpt.get().getName(), "streak", userOpt.get().getCurrentStreak() != null ? userOpt.get().getCurrentStreak() : 0));
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
        String googleToken = body.get("token");
        if (googleToken == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token is required"));
        }

        try {
            com.google.api.client.http.HttpTransport transport = new com.google.api.client.http.javanet.NetHttpTransport();
            com.google.api.client.json.JsonFactory jsonFactory = com.google.api.client.json.gson.GsonFactory.getDefaultInstance();
            
            // Note: For production, you should set the Audience to your specific Client ID.
            com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier verifier = new com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .build();

            com.google.api.client.googleapis.auth.oauth2.GoogleIdToken idToken = verifier.verify(googleToken);
            if (idToken != null) {
                com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                Optional<User> userOpt = userRepository.findByEmail(email);
                User user;
                if (userOpt.isEmpty()) {
                    user = new User();
                    user.setEmail(email);
                    user.setName(name);
                    user.setPassword(passwordEncoder.encode(java.util.UUID.randomUUID().toString())); // Random password
                    userRepository.save(user);
                } else {
                    user = userOpt.get();
                }

                String jwt = jwtService.generateToken(email);
                return ResponseEntity.ok(Map.of("token", jwt, "email", email, "name", user.getName(), "streak", user.getCurrentStreak() != null ? user.getCurrentStreak() : 0));
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid Google token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error verifying Google token", "details", e.getMessage()));
        }
    }
}
