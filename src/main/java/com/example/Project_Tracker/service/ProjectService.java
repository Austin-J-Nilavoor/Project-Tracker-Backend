package com.example.Project_Tracker.service;

import com.example.Project_Tracker.dto.ProjectDto;
import com.example.Project_Tracker.entity.Ibu;
import com.example.Project_Tracker.entity.Project;
import com.example.Project_Tracker.entity.User;
import com.example.Project_Tracker.repository.IbuRepository;
import com.example.Project_Tracker.repository.ProjectRepository;
import com.example.Project_Tracker.repository.UserRepository;
import com.example.Project_Tracker.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final IbuRepository ibuRepository;
    private final UserRepository userRepository;
    private final MapperUtils mapperUtils;

    // ✅ Get all projects
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ✅ Get single project
    public ProjectDto getProjectById(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return convertToDto(project);
    }

    // ✅ Create project from DTO
    public ProjectDto createProjectFromDto(ProjectDto dto) {
        Project project = convertToEntity(dto);
        project.setCreatedAt(LocalDateTime.now());
        Project saved = projectRepository.save(project);
        return convertToDto(saved);
    }

    // ✅ Update project from DTO
    public ProjectDto updateProjectFromDto(String id, ProjectDto dto) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setPriority(dto.getPriority());
        existing.setEndDate(dto.getEndDate());

        if (dto.getManagerId() != null) {
            User manager = userRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            existing.setManager(manager);
        }

        if (dto.getIbuId() != null) {
            Ibu ibu = ibuRepository.findById(dto.getIbuId())
                    .orElseThrow(() -> new RuntimeException("IBU not found"));
            existing.setIbu(ibu);
        }
        existing.setUpdatedAt(LocalDateTime.now());
        Project updated = projectRepository.save(existing);
        return convertToDto(updated);
    }

    // ✅ Delete project
    public void deleteProject(String id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found");
        }
        projectRepository.deleteById(id);
    }

    // ✅ Projects by manager
    public List<ProjectDto> getProjectsByManager(String managerId) {
        return projectRepository.findByManager_Id(managerId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ✅ Entity → DTO conversion
    private ProjectDto convertToDto(Project project) {
        ProjectDto dto = mapperUtils.map(project, ProjectDto.class);

        if (project.getIbu() != null) {
            dto.setIbuId(project.getIbu().getId());
            dto.setIbuName(project.getIbu().getName());
        }

        if (project.getManager() != null) {
            dto.setManagerId(project.getManager().getId());
            dto.setManagerName(project.getManager().getName());
        }

        return dto;
    }

    // ✅ DTO → Entity conversion
    private Project convertToEntity(ProjectDto dto) {
        Project project = mapperUtils.map(dto, Project.class);

        if (dto.getIbuId() != null) {
            Ibu ibu = ibuRepository.findById(dto.getIbuId())
                    .orElseThrow(() -> new RuntimeException("IBU not found"));
            project.setIbu(ibu);
        }

        if (dto.getManagerId() != null) {
            User manager = userRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            project.setManager(manager);
        }

        return project;
    }
}
