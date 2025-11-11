package com.example.Project_Tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "project_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ibu_id", nullable = false)
    private Ibu ibu;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(length = 25)
    private String status; // Open, In Progress, Completed

    @Column(length = 15)
    private String priority; // Low, Medium, High, Critical

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "manager_user_id", nullable = false)
    // private User manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Milestone> milestones;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectMember> members;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;
}
