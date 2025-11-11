package com.example.Project_Tracker.controller;

import com.example.Project_Tracker.dto.ApiResponse;
import com.example.Project_Tracker.dto.ProjectMemberDto;
import com.example.Project_Tracker.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<ProjectMemberDto>>> getMembersByProject(@PathVariable String projectId) {
        List<ProjectMemberDto> members = projectMemberService.getMembersByProject(projectId);

        ApiResponse<List<ProjectMemberDto>> response = ApiResponse.<List<ProjectMemberDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Project members fetched successfully")
                .data(members)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectMemberDto>> addMember(@RequestBody ProjectMemberDto dto) {
        ProjectMemberDto created = projectMemberService.addMember(dto);

        ApiResponse<ProjectMemberDto> response = ApiResponse.<ProjectMemberDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Member added successfully")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/project/{projectId}/user/{userId}")
    public ResponseEntity<ApiResponse<String>> removeMember(@PathVariable String projectId, @PathVariable String userId) {
        projectMemberService.removeMember(projectId, userId);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Member removed successfully")
                .data("Removed member " + userId + " from project " + projectId)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
