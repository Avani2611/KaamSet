package com.kaamSet.backend.service;

import com.kaamSet.backend.model.User;
import com.kaamSet.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.kaamSet.backend.dto.UserProfileResponse;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

public UserProfileResponse getCurrentUserProfile() {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    String email = authentication.getName();

    User user = userRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return new UserProfileResponse(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getRole(),
            user.isActive()
    );
}

}
