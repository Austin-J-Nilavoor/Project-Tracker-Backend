package com.example.Project_Tracker.service;

import com.example.Project_Tracker.dto.ProjectMemberDto;
import com.example.Project_Tracker.entity.Project;
import com.example.Project_Tracker.entity.ProjectMember;
import com.example.Project_Tracker.entity.ProjectMemberId;
import com.example.Project_Tracker.entity.User;
import com.example.Project_Tracker.repository.ProjectMemberRepository;
import com.example.Project_Tracker.repository.ProjectRepository;
import com.example.Project_Tracker.repository.UserRepository;
import com.example.Project_Tracker.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final MapperUtils mapperUtils;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ✅ Get all members of a project
    public List<ProjectMemberDto> getMembersByProject(String projectId) {
        return projectMemberRepository.findByProject_Id(projectId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ✅ Add a new project member
    public ProjectMemberDto addMember(ProjectMemberDto dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);
        member.setRoleInProject(dto.getRole());

        ProjectMember saved = projectMemberRepository.save(member);
        return convertToDto(saved);
    }

    // ✅ Remove a project member
    public void removeMember(String projectId, String userId) {
        ProjectMemberId id = new ProjectMemberId(projectId, userId);
        if (!projectMemberRepository.existsById(id)) {
            throw new RuntimeException("Project member not found");
        }
        projectMemberRepository.deleteById(id);
    }

    // ✅ Convert entity → DTO
    private ProjectMemberDto convertToDto(ProjectMember member) {
        ProjectMemberDto dto = mapperUtils.map(member, ProjectMemberDto.class);

        if (member.getProject() != null) {
            dto.setProjectId(member.getProject().getId());
            dto.setProjectName(member.getProject().getName());
        }

        if (member.getUser() != null) {
            dto.setUserId(member.getUser().getId());
            dto.setUserName(member.getUser().getName());
        }

        if (member.getJoinedAt() != null) {
            dto.setJoinedAt(member.getJoinedAt().format(formatter));
        }

        return dto;
    }
}
