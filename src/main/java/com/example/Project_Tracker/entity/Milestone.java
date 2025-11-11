package com.example.Project_Tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "milestones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "milestone_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(length = 25)
    private String status; // Planned, Active, Completed

    private Integer progress = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depends_on_milestone_id")
    private Milestone dependsOn;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "milestone", cascade = CascadeType.ALL)
    private List<Task> tasks;
}
