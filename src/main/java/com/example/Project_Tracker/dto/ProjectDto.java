package com.example.Project_Tracker.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private String id;
    private String name;
    private String description;
    private String status;
    private String priority;
    private LocalDate startDate;
    private LocalDate endDate;

    // IBU Info
    private String ibuId;
    private String ibuName;

    // Manager Info
    private String managerId;
    private String managerName;
}
