package com.example.Project_Tracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String role;     // ADMIN, MANAGER, PROJECT_MANAGER, EMPLOYEE
    private String ibuName;    // Optional for assigning department
}
