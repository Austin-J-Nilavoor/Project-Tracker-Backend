package com.example.Project_Tracker.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String id;

    private String name;
    private String email;
    private String role; // ADMIN, MANAGER, PROJECT_MANAGER, EMPLOYEE

    private String ibuId;   // Linked IBU ID
    private String ibuName; // Display name of IBU
}
