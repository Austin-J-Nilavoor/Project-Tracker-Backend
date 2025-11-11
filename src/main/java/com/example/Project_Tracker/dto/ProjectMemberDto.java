package com.example.Project_Tracker.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberDto {
    private String projectId;
    private String projectName;

    private String userId;
    private String userName;

    private String role; // e.g., PROJECT_MANAGER, DEVELOPER, TESTER
    private String joinedAt;
}
