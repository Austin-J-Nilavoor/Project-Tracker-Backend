package com.example.Project_Tracker.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String name;
    private String email;
    private String role;

    // 🔹 Flattened IBU relation
    private String ibuId;
    private String ibuName;
}
