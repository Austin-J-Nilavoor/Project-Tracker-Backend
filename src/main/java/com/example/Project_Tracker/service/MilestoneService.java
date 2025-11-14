package com.example.Project_Tracker.service;

import com.example.Project_Tracker.dto.MilestoneDto;
import com.example.Project_Tracker.entity.Milestone;
import com.example.Project_Tracker.entity.Project;
import com.example.Project_Tracker.entity.User;
import com.example.Project_Tracker.enums.ProjectRole;
import com.example.Project_Tracker.enums.Status;
import com.example.Project_Tracker.exceptions.ResourceNotFoundException;
import com.example.Project_Tracker.repository.MilestoneRepository;
import com.example.Project_Tracker.repository.ProjectMemberRepository;
import com.example.Project_Tracker.repository.ProjectRepository;
import com.example.Project_Tracker.repository.UserRepository;
import com.example.Project_Tracker.utils.MapperUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final MapperUtils mapperUtils;

    // ✅ Get all milestones for a project
    public List<MilestoneDto> getMilestonesByProject(String projectId) {
        return milestoneRepository.findByProject_Id(projectId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ✅ Create milestone — only Manager or Project Manager allowed
    public MilestoneDto createMilestone(MilestoneDto dto) {
        authorizeProjectAccess(dto.getProjectId());

        Milestone milestone = convertToEntity(dto);

        if (dto.getDependsOnId() != null) {
            Milestone dependency = milestoneRepository.findById(dto.getDependsOnId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dependency milestone not found"));
            milestone.setDependsOn(dependency);
        }

        // milestone.setCreatedAt(LocalDateTime.now());
        Milestone saved = milestoneRepository.save(milestone);
        return convertToDto(saved);
    }

    // ✅ Update milestone — only Manager or Project Manager allowed
    public MilestoneDto updateMilestone(String id, MilestoneDto dto) {
        Milestone existing = milestoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone not found"));

        // Authorization check using project ID
        authorizeProjectAccess(existing.getProject().getId());

// Only update the name if the DTO provided a non-null value
        if (dto.getName() != null) {
            existing.setName(dto.getName());
        }

        // Only update the description if the DTO provided a non-null value
        if (dto.getDescription() != null) {
            existing.setDescription(dto.getDescription());
        }

        // Only update the status if the DTO provided a non-null value
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        }

        // Only update the start date if the DTO provided a non-null value
        if (dto.getStartDate() != null) {
            existing.setStartDate(dto.getStartDate());
        }

        // Only update the end date if the DTO provided a non-null value
        if (dto.getEndDate() != null) {
            existing.setEndDate(dto.getEndDate());
        }

        if (dto.getDependsOnId() != null) {
            Milestone dependency = milestoneRepository.findById(dto.getDependsOnId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dependency milestone not found"));
            existing.setDependsOn(dependency);
        } else {
            existing.setDependsOn(null);
        }

        if (dto.getStatus() == Status.IN_PROGRESS) {
            if (existing.getDependsOn() != null &&
                    existing.getDependsOn().getStatus() != Status.COMPLETED) {
                throw new IllegalStateException("Cannot start this milestone until the dependent one is completed");
            }
        }

        existing.setUpdatedAt(LocalDateTime.now());
        Milestone updated = milestoneRepository.save(existing);
        return convertToDto(updated);
    }

    // ✅ Delete milestone — only Manager or Project Manager allowed
    public void deleteMilestone(String id) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone not found"));

        // Authorization check using project ID
        authorizeProjectAccess(milestone.getProject().getId());

        milestoneRepository.deleteById(id);
    }

    // ✅ Common method — Authorize current user for a given project
    private void authorizeProjectAccess(String projectId) {
        User currentUser = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isProjectManager = projectMemberRepository
                .existsById_ProjectIdAndUser_IdAndRoleInProject(projectId, currentUser.getId(), ProjectRole.valueOf("PROJECT_MANAGER"));
        boolean isSameIBU = false;
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        if (currentUser.getIbu() != null && project.getIbu() != null) {
            isSameIBU = currentUser.getIbu().getId().equals(project.getIbu().getId());
        }
        if ((!currentUser.getRole().equalsIgnoreCase("MANAGER")||!isSameIBU) && !isProjectManager) {
            throw new AccessDeniedException("You are not authorized to access or modify milestones for this project");
        }
    }

    // ✅ Entity → DTO conversion
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

    // ✅ DTO → Entity conversion
    private Milestone convertToEntity(MilestoneDto dto) {
        Milestone milestone = mapperUtils.map(dto, Milestone.class);

        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
            milestone.setProject(project);
        }

        if (dto.getDependsOnId() != null) {
            Milestone dependency = milestoneRepository.findById(dto.getDependsOnId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dependency milestone not found"));
            milestone.setDependsOn(dependency);
        }

        return milestone;
    }
}
