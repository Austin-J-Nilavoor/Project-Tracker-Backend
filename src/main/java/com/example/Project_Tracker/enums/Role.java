package com.example.Project_Tracker.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents the role of a user within a specific project.")
public enum Role {
    ADMIN,
    MANAGER,
    EMPLOYEE
}