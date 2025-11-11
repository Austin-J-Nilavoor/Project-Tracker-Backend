package com.example.Project_Tracker.service;

import com.example.Project_Tracker.dto.TaskDto;
import com.example.Project_Tracker.entity.Project;
import com.example.Project_Tracker.entity.Task;
import com.example.Project_Tracker.entity.User;
import com.example.Project_Tracker.repository.ProjectRepository;
import com.example.Project_Tracker.repository.TaskRepository;
import com.example.Project_Tracker.repository.UserRepository;
import com.example.Project_Tracker.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final MapperUtils mapperUtils;

    // 🔹 Get tasks by project
    public List<TaskDto> getTasksByProject(String projectId) {
        return taskRepository.findByProject_Id(projectId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 🔹 Get tasks by user
    public List<TaskDto> getTasksByUser(String userId) {
        return taskRepository.findByAssignedTo_Id(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 🔹 Create task
    public TaskDto createTask(TaskDto taskDto) {
        Task task = convertToEntity(taskDto);
        Task savedTask = taskRepository.save(task);
        return convertToDto(savedTask);
    }

    // 🔹 Update task
    public TaskDto updateTask(String id, TaskDto updatedTaskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(updatedTaskDto.getTitle());
        task.setDescription(updatedTaskDto.getDescription());
        task.setStatus(updatedTaskDto.getStatus());
        task.setPriority(updatedTaskDto.getPriority());
        task.setDueDate(updatedTaskDto.getDueDate());

        // optional reassignment
        if (updatedTaskDto.getAssignedToId() != null) {
            User user = userRepository.findById(updatedTaskDto.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignedTo(user);
        }

        Task updated = taskRepository.save(task);
        return convertToDto(updated);
    }

    // 🔹 Update only task status
    public TaskDto updateTaskStatus(String id, String status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        Task updated = taskRepository.save(task);
        return convertToDto(updated);
    }

    // 🔹 Delete task safely
    public boolean deleteTask(String id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }
        taskRepository.deleteById(id);
        return true;
    }

    // 🔹 Convert Entity → DTO
    private TaskDto convertToDto(Task task) {
        TaskDto dto = mapperUtils.map(task, TaskDto.class);

        if (task.getProject() != null) {
            dto.setProjectId(task.getProject().getId());
            dto.setProjectName(task.getProject().getName());
        }

        if (task.getAssignedTo() != null) {
            dto.setAssignedToId(task.getAssignedTo().getId());
            dto.setAssignedToName(task.getAssignedTo().getName());
        }

        return dto;
    }

    // 🔹 Convert DTO → Entity
    private Task convertToEntity(TaskDto dto) {
        Task task = mapperUtils.map(dto, Task.class);

        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            task.setProject(project);
        }

        if (dto.getAssignedToId() != null) {
            User user = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            task.setAssignedTo(user);
        }

        return task;
    }
}
