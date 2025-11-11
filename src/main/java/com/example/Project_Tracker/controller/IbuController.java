package com.example.Project_Tracker.controller;

import com.example.Project_Tracker.dto.ApiResponse;
import com.example.Project_Tracker.dto.IbuDto;
import com.example.Project_Tracker.service.IbuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ibus")
@RequiredArgsConstructor
public class IbuController {

    private final IbuService ibuService;

    // ✅ Get all IBUs
    @GetMapping
    public ResponseEntity<ApiResponse<List<IbuDto>>> getAllIbus() {
        List<IbuDto> ibus = ibuService.getAllIbus();

        ApiResponse<List<IbuDto>> response = ApiResponse.<List<IbuDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Fetched all IBUs successfully")
                .data(ibus)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    // ✅ Create new IBU
    @PostMapping
    public ResponseEntity<ApiResponse<IbuDto>> createIbu(@RequestBody IbuDto ibuDto) {
        IbuDto newIbu = ibuService.createIbu(ibuDto);

        ApiResponse<IbuDto> response = ApiResponse.<IbuDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("IBU created successfully")
                .data(newIbu)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ✅ Delete IBU by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteIbu(@PathVariable String id) {
        ibuService.deleteIbu(id);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("IBU deleted successfully")
                .data(id)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
