package com.example.test.Controller;

import com.example.test.Entity.User;
import com.example.test.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")

public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User newUser) {
        try {
            // Check if the username (email) already exists in the database
            User existingUser = userRepository.findByEmail(newUser.getEmail());
            if (existingUser != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }

            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

            // Save the new user to the database
            userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"User registered successfully\"}");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        try {
            // Find the user in the database by username (email)
            User existingUser = userRepository.findByEmail(user.getEmail());

            // Check if the user exists and the provided password matches
            if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                // Authentication successful
                return ResponseEntity.ok("Login successful");
            } else {
                // Authentication failed
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
    }


}
