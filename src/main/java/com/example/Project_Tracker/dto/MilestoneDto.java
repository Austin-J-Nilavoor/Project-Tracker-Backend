package com.example.Project_Tracker.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

import com.example.Project_Tracker.enums.Status;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilestoneDto {

    private String id; // optional for create, required for update

    @NotBlank(message = "Milestone name is required")
    @Size(min = 3, max = 100, message = "Milestone name must be between 3 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Status is required (Pending, In_Progress, Completed)")
    private Status status; 

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotBlank(message = "Project ID is required")
    private String projectId;

    private String projectName; // auto-filled for responses

    private String dependsOnId;   // optional dependency milestone
    private String dependsOnName; // optional
}
