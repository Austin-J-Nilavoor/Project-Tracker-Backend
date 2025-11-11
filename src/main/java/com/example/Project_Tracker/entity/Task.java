package com.example.Project_Tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.Project_Tracker.enums.TaskPriority;
import com.example.Project_Tracker.enums.TaskStatus;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "task_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id")
    private Milestone milestone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task_id")
    private Task parentTask; // For subtasks

    @Column(nullable = false, length = 128)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 25)
    private TaskStatus status; // To Do, In Progress, Review, Completed

    @Column(length = 15)
    private TaskPriority priority;

    private LocalDate dueDate;

    private final LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
