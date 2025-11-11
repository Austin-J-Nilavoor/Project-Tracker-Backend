package com.example.Project_Tracker.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilestoneDto {
    private String id;
    private String name;
    private String description;
    private String status;      // Pending, In Progress, Completed
    private LocalDate startDate;
    private LocalDate endDate;

    private String projectId;
    private String projectName;

    private String dependsOnId;
    private String dependsOnName;
}
