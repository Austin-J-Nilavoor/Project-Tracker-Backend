package com.example.Project_Tracker.controller;

import com.example.Project_Tracker.dto.ApiResponse;
import com.example.Project_Tracker.dto.ProjectDto;
import com.example.Project_Tracker.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectDto>>> getAllProjects() {
        List<ProjectDto> projects = projectService.getAllProjects();

        ApiResponse<List<ProjectDto>> response = ApiResponse.<List<ProjectDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Projects fetched successfully")
                .data(projects)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDto>> getProject(@PathVariable String id) {
        ProjectDto project = projectService.getProjectById(id);

        ApiResponse<ProjectDto> response = ApiResponse.<ProjectDto>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Project fetched successfully")
                .data(project)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectDto>> createProject(@RequestBody ProjectDto projectDto) {
        ProjectDto created = projectService.createProjectFromDto(projectDto);

        ApiResponse<ProjectDto> response = ApiResponse.<ProjectDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Project created successfully")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDto>> updateProject(
            @PathVariable String id,
            @RequestBody ProjectDto projectDto
    ) {
        ProjectDto updated = projectService.updateProjectFromDto(id, projectDto);

        ApiResponse<ProjectDto> response = ApiResponse.<ProjectDto>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Project updated successfully")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Project deleted successfully")
                .data(id)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    // @GetMapping("/manager/{managerId}")
    // public ResponseEntity<ApiResponse<List<ProjectDto>>> getProjectsByManager(@PathVariable String managerId) {
    //     List<ProjectDto> projects = projectService.getProjectsByManager(managerId);

    //     ApiResponse<List<ProjectDto>> response = ApiResponse.<List<ProjectDto>>builder()
    //             .statusCode(HttpStatus.OK.value())
    //             .success(true)
    //             .message("Projects for manager fetched successfully")
    //             .data(projects)
    //             .timestamp(LocalDateTime.now())
    //             .build();

    //     return ResponseEntity.ok(response);
    // }
}
