package com.basit.cz.jwtandfileuploadinginspringboot.service;

import com.basit.cz.jwtandfileuploadinginspringboot.dto.JwtResponse;
import com.basit.cz.jwtandfileuploadinginspringboot.dto.LoginRequest;
import com.basit.cz.jwtandfileuploadinginspringboot.dto.SignupRequest;
import com.basit.cz.jwtandfileuploadinginspringboot.entity.User;
import com.basit.cz.jwtandfileuploadinginspringboot.repository.UserRepository;
import com.basit.cz.jwtandfileuploadinginspringboot.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public JwtResponse login(LoginRequest loginRequest) {
        System.out.println("Login attempt for username: " + loginRequest.getUsername());

        // Check if user exists first
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> {
                    System.out.println("User not found: " + loginRequest.getUsername());
                    return new RuntimeException("User not found");
                });

        System.out.println("User found: " + user.getUsername());
        System.out.println("Stored password hash: " + user.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            System.out.println("Authentication successful for: " + user.getUsername());
            return new JwtResponse(jwt, user.getUsername(), user.getEmail());

        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            throw new RuntimeException("Invalid credentials");
        }
    }

    public String signup(SignupRequest signupRequest) {
        // Check if username exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        // Check if email exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new user
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        System.out.println("Original password: " + signupRequest.getPassword());
        System.out.println("Encoded password: " + encodedPassword);

        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                encodedPassword
        );

        userRepository.save(user);
        System.out.println("User saved successfully: " + user.getUsername());
        return "User registered successfully!";
    }
}
