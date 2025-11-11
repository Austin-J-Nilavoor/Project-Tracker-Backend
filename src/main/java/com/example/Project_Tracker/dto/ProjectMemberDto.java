package com.example.Project_Tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

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

    @NotBlank(message = "Role is required (e.g., PROJECT_MANAGER, DEVELOPER, TESTER)")
    @Pattern(
        regexp = "PROJECT_MANAGER|DEVELOPER|TESTER|DESIGNER|ANALYST",
        message = "Role must be one of: PROJECT_MANAGER, DEVELOPER, TESTER, DESIGNER, ANALYST"
    )
    private String role;

    @NotNull(message = "Join date is required")
    private LocalDateTime joinedAt;
}
