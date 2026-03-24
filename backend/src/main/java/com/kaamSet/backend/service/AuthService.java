package com.kaamSet.backend.service;

import com.kaamSet.backend.dto.AuthResponse;
import com.kaamSet.backend.dto.LoginRequest;
import com.kaamSet.backend.dto.RegisterRequest;
import com.kaamSet.backend.dto.WorkerRegisterRequest;
import com.kaamSet.backend.model.Role;
import com.kaamSet.backend.model.User;
import com.kaamSet.backend.repository.UserRepository;
import com.kaamSet.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =========================
    // USER REGISTRATION
    // =========================
    public void register(RegisterRequest request) {

    if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
        throw new RuntimeException("Email already registered");
    }

    User user = User.builder()
            .fullName(request.getFullName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .city(request.getCity())
            .area(request.getArea())
            .pincode(request.getPincode())
            .active(true)
            .build();

        userRepository.save(user);
    }

    // =========================
    // WORKER REGISTRATION
    // =========================
    public void registerWorker(WorkerRegisterRequest request) {

    if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
        throw new RuntimeException("Email already registered");
    }

        User worker = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.WORKER)
                .city(request.getCity())
                .area(request.getArea())
                .pincode(request.getPincode())
                .active(true)
                .build();

        userRepository.save(worker);
    }

    // =========================
    // LOGIN
    // =========================
public AuthResponse login(LoginRequest request) {

    User user = userRepository.findByEmailIgnoreCase(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new RuntimeException("Invalid credentials");
    }

    String token = jwtService.generateToken(user);

    return new AuthResponse(token, "Login successful", user.getRole().name());
}
}
