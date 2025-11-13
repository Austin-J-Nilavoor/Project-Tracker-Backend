package com.example.Project_Tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

import com.example.Project_Tracker.enums.ProjectRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberDto {

    @NotBlank(message = "Project ID is required")
    private String projectId;

    private String projectName;

    @NotBlank(message = "User ID is required")
    private String userId;

    private String userName;

    @NotNull(message = "Role is required (e.g., PROJECT_MANAGER, DEVELOPER, TESTER)")
    private ProjectRole role;

    private LocalDateTime joinedAt;
}
