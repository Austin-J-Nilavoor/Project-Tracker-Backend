package com.example.Project_Tracker.exceptions;

import com.example.Project_Tracker.dto.ApiResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // works for @RestController automatically
public class GlobalExceptionHandler {

    // ✅ Generic exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ✅ Handle validation errors (e.g., @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .success(false)
                .message("Validation failed")
                .data(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ✅ Custom exception example
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
         System.out.println("🟢 Handling ResourceNotFoundException: " + ex.getMessage());
        ApiResponse<String> response = ApiResponse.<String>builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

@ExceptionHandler(DataIntegrityViolationException.class)
public ResponseEntity<ApiResponse<String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    ApiResponse<String> response = ApiResponse.<String>builder()
            .statusCode(HttpStatus.CONFLICT.value())
            .success(false)
            .message("Cannot delete this IBU because it is linked to existing users or projects.")
            .data(null)
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
}

}
