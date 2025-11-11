package com.example.Project_Tracker.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    private String id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDate dueDate;

    // 🔹 Relations (flattened for frontend convenience)
    private String projectId;
    private String projectName;

    private String assignedToId;
    private String assignedToName;
}
