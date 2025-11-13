package com.example.Project_Tracker.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

import com.example.Project_Tracker.enums.Priority;
import com.example.Project_Tracker.enums.TaskStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {

    private String id; // Auto-generated, no need for validation on create

    @NotBlank(message = "Task title is required")
    private String title;

    @NotBlank(message = "Task description is required")
    private String description;

    @NotNull(message = "Task status is required")
    private TaskStatus status;

    @NotNull(message = "Task priority is required")
    private Priority priority;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date cannot be in the past")
    private LocalDate dueDate;

    // 🔹 Relations (flattened for frontend convenience)
    @NotBlank(message = "Project ID is required")
    private String projectId;

    private String projectName; // optional (read-only convenience for frontend)

    private String assignedToId;
    private String assignedToName;

    @NotBlank(message = "Milestone ID is required")
    private String milestoneID;
}
