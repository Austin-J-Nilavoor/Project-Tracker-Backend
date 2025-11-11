package com.example.Project_Tracker.controller;

import com.example.Project_Tracker.dto.ApiResponse;
import com.example.Project_Tracker.dto.TaskDto;
import com.example.Project_Tracker.enums.TaskStatus;
import com.example.Project_Tracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<TaskDto>>> getTasksByProject(@PathVariable String projectId) {
        List<TaskDto> tasks = taskService.getTasksByProject(projectId);

        ApiResponse<List<TaskDto>> response = ApiResponse.<List<TaskDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Tasks for project fetched successfully")
                .data(tasks)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TaskDto>>> getTasksByUser(@PathVariable String userId) {
        List<TaskDto> tasks = taskService.getTasksByUser(userId);

        ApiResponse<List<TaskDto>> response = ApiResponse.<List<TaskDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Tasks for user fetched successfully")
                .data(tasks)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskDto>> createTask(@RequestBody TaskDto taskDto) {
        TaskDto created = taskService.createTask(taskDto);

        ApiResponse<TaskDto> response = ApiResponse.<TaskDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Task created successfully")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDto>> updateTask(@PathVariable String id, @RequestBody TaskDto taskDto) {
        TaskDto updated = taskService.updateTask(id, taskDto);

        ApiResponse<TaskDto> response = ApiResponse.<TaskDto>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Task updated successfully")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TaskDto>> updateTaskStatus(@PathVariable String id, @RequestParam TaskStatus status) {
        TaskDto updated = taskService.updateTaskStatus(id, status);

        ApiResponse<TaskDto> response = ApiResponse.<TaskDto>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Task status updated successfully")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTask(@PathVariable String id) {
        boolean deleted = taskService.deleteTask(id);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .statusCode(deleted ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value())
                .success(deleted)
                .message(deleted ? "Task deleted successfully" : "Task not found")
                .data(id)
                .timestamp(LocalDateTime.now())
                .build();

        return deleted ? ResponseEntity.ok(response)
                       : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
