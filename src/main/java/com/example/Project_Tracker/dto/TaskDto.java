package com.example.Project_Tracker.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class TaskDto {
    private String id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDate dueDate;
    private String projectId;
    private String assignedToId;
}
