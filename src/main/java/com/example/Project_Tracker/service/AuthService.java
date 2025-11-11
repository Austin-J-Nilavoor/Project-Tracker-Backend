package com.example.Project_Tracker.service;

import com.example.Project_Tracker.dto.AuthRequest;
import com.example.Project_Tracker.dto.AuthResponse;
import com.example.Project_Tracker.dto.RegisterRequest;
import com.example.Project_Tracker.dto.UserDto;
import com.example.Project_Tracker.entity.Ibu;
import com.example.Project_Tracker.entity.User;
import com.example.Project_Tracker.repository.IbuRepository;
import com.example.Project_Tracker.repository.UserRepository;
import com.example.Project_Tracker.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final IbuRepository ibuRepository;

    public UserDto register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User with email '" + request.getEmail() + "' already exists");
        }

        Ibu ibu = ibuRepository.findByName(request.getIbuName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "IBU not found with name: " + request.getIbuName()));

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .ibu(ibu)
                .build();

        userRepository.save(user);

        // 🔹 Return DTO (flattened structure)
        return UserDto.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .ibuId(ibu.getId().toString())
                .ibuName(ibu.getName())
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(token, user.getRole(), user.getName());
    }
}
