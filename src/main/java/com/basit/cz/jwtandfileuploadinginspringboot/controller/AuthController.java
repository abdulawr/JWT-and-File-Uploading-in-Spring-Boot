package com.basit.cz.jwtandfileuploadinginspringboot.controller;


import com.basit.cz.jwtandfileuploadinginspringboot.dto.JwtResponse;
import com.basit.cz.jwtandfileuploadinginspringboot.dto.LoginRequest;
import com.basit.cz.jwtandfileuploadinginspringboot.dto.MessageResponse;
import com.basit.cz.jwtandfileuploadinginspringboot.dto.SignupRequest;
import com.basit.cz.jwtandfileuploadinginspringboot.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.login(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Invalid username or password!"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            String message = authService.signup(signupRequest);
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(new MessageResponse("Hello " + authentication.getName() + "!"));
        }
        return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: User not authenticated"));
    }
}