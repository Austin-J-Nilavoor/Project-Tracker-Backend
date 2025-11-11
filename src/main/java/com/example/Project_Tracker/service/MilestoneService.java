package com.example.Project_Tracker.service;

import com.example.Project_Tracker.dto.MilestoneDto;
import com.example.Project_Tracker.entity.Milestone;
import com.example.Project_Tracker.entity.Project;
import com.example.Project_Tracker.repository.MilestoneRepository;
import com.example.Project_Tracker.repository.ProjectRepository;
import com.example.Project_Tracker.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;
    private final MapperUtils mapperUtils;

    // ✅ Get milestones by project
    public List<MilestoneDto> getMilestonesByProject(String projectId) {
        return milestoneRepository.findByProject_Id(projectId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ✅ Create milestone from DTO
    public MilestoneDto createMilestone(MilestoneDto dto) {
        Milestone milestone = convertToEntity(dto);

        if (dto.getDependsOnId() != null) {
            Milestone dependency = milestoneRepository.findById(dto.getDependsOnId())
                    .orElseThrow(() -> new RuntimeException("Dependency milestone not found"));
            milestone.setDependsOn(dependency);
        }
        milestone.setCreatedAt(LocalDateTime.now());
        Milestone saved = milestoneRepository.save(milestone);
        return convertToDto(saved);
    }

    // ✅ Update milestone from DTO
    public MilestoneDto updateMilestone(String id, MilestoneDto dto) {
        Milestone existing = milestoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Milestone not found"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());

        if (dto.getDependsOnId() != null) {
            Milestone dependency = milestoneRepository.findById(dto.getDependsOnId())
                    .orElseThrow(() -> new RuntimeException("Dependency milestone not found"));
            existing.setDependsOn(dependency);
        } else {
            existing.setDependsOn(null);
        }
        if ("IN_PROGRESS".equalsIgnoreCase(dto.getStatus())) {
            if (existing.getDependsOn() != null &&
                    !"COMPLETED".equalsIgnoreCase(existing.getDependsOn().getStatus())) {
                throw new RuntimeException("Cannot start this milestone until the dependent one is completed");
            }
        }
        existing.setUpdatedAt(LocalDateTime.now());
        Milestone updated = milestoneRepository.save(existing);
        return convertToDto(updated);
    }

    // ✅ Delete milestone safely
    public void deleteMilestone(String id) {
        if (!milestoneRepository.existsById(id)) {
            throw new RuntimeException("Milestone not found");
        }
        milestoneRepository.deleteById(id);
    }

    // ✅ Entity → DTO
    private MilestoneDto convertToDto(Milestone milestone) {
        MilestoneDto dto = mapperUtils.map(milestone, MilestoneDto.class);

        if (milestone.getProject() != null) {
            dto.setProjectId(milestone.getProject().getId());
            dto.setProjectName(milestone.getProject().getName());
        }

        if (milestone.getDependsOn() != null) {
            dto.setDependsOnId(milestone.getDependsOn().getId());
            dto.setDependsOnName(milestone.getDependsOn().getName());
        }

        return dto;
    }

    // ✅ DTO → Entity
    private Milestone convertToEntity(MilestoneDto dto) {
        Milestone milestone = mapperUtils.map(dto, Milestone.class);

        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            milestone.setProject(project);
        }

        if (dto.getDependsOnId() != null) {
            Milestone dependency = milestoneRepository.findById(dto.getDependsOnId())
                    .orElseThrow(() -> new RuntimeException("Dependency milestone not found"));
            milestone.setDependsOn(dependency);
        }

        return milestone;
    }
}
