package com.example.Project_Tracker.controller;

import com.example.Project_Tracker.dto.ApiResponse;
import com.example.Project_Tracker.dto.AuthRequest;
import com.example.Project_Tracker.dto.AuthResponse;
import com.example.Project_Tracker.dto.RegisterRequest;
import com.example.Project_Tracker.dto.UserDto;
import com.example.Project_Tracker.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody RegisterRequest request) {
        UserDto userDto = authService.register(request);

        ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                .statusCode(200)
                .success(true)
                .message("User registered successfully")
                .data(userDto)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
