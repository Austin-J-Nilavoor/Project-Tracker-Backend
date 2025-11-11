package com.example.Project_Tracker.controller;

import com.example.Project_Tracker.dto.ApiResponse;
import com.example.Project_Tracker.dto.MilestoneDto;
import com.example.Project_Tracker.service.MilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/milestones")
@RequiredArgsConstructor
public class MilestoneController {

    private final MilestoneService milestoneService;

    // ✅ Get milestones by project
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<MilestoneDto>>> getMilestonesByProject(@PathVariable String projectId) {
        List<MilestoneDto> milestones = milestoneService.getMilestonesByProject(projectId);

        ApiResponse<List<MilestoneDto>> response = ApiResponse.<List<MilestoneDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Milestones fetched successfully")
                .data(milestones)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    // ✅ Create milestone
    @PostMapping
    public ResponseEntity<ApiResponse<MilestoneDto>> createMilestone(@RequestBody MilestoneDto milestoneDto) {
        MilestoneDto created = milestoneService.createMilestone(milestoneDto);

        ApiResponse<MilestoneDto> response = ApiResponse.<MilestoneDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Milestone created successfully")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ Update milestone
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MilestoneDto>> updateMilestone(
            @PathVariable String id,
            @RequestBody MilestoneDto milestoneDto
    ) {
        MilestoneDto updated = milestoneService.updateMilestone(id, milestoneDto);

        ApiResponse<MilestoneDto> response = ApiResponse.<MilestoneDto>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Milestone updated successfully")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    // ✅ Delete milestone
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMilestone(@PathVariable String id) {
        milestoneService.deleteMilestone(id);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Milestone deleted successfully")
                .data(id)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
