package com.kaamSet.backend.controller;

import com.kaamSet.backend.dto.AuthResponse;
import com.kaamSet.backend.dto.LoginRequest;
import com.kaamSet.backend.dto.RegisterRequest;
import com.kaamSet.backend.dto.WorkerRegisterRequest;
import com.kaamSet.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =========================
    // USER REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request);

        return ResponseEntity.ok(
                new AuthResponse(
                        null,
                        "User registered successfully",
                        null
                )
        );
    }

    // =========================
    // WORKER REGISTER
    // =========================
    @PostMapping("/register/worker")
    public ResponseEntity<AuthResponse> registerWorker(
            @Valid @RequestBody WorkerRegisterRequest request) {

        authService.registerWorker(request);

        return ResponseEntity.ok(
                new AuthResponse(
                        null,
                        "Worker registered successfully",
                        null
                )
        );
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}