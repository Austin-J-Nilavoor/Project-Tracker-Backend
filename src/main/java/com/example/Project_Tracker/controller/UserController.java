package com.example.Project_Tracker.controller;

import com.example.Project_Tracker.dto.ApiResponse;
import com.example.Project_Tracker.dto.UserDto;
import com.example.Project_Tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();

        ApiResponse<List<UserDto>> response = ApiResponse.<List<UserDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Users fetched successfully")
                .data(users)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable String id) {
        UserDto user = userService.getUserById(id);

        ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("User details fetched successfully")
                .data(user)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable String id,
            @RequestBody UserDto user
    ) {
        UserDto updated = userService.updateUser(id, user);

        ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("User updated successfully")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("User deleted successfully")
                .data(id)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
