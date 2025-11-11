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

    @NotBlank(message = "Project name is required")
    private String projectName;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "User name is required")
    private String userName;

    @NotNull(message = "Role is required (e.g., PROJECT_MANAGER, DEVELOPER, TESTER)")
    private ProjectRole role;

    @NotNull(message = "Join date is required")
    private LocalDateTime joinedAt;
}
