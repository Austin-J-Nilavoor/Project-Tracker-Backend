package com.example.Project_Tracker.service;

import com.example.Project_Tracker.dto.TaskDto;
import com.example.Project_Tracker.entity.Task;
import com.example.Project_Tracker.repository.TaskRepository;
import com.example.Project_Tracker.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final MapperUtils mapperUtils;

    // 🔹 Get tasks by project
    public List<TaskDto> getTasksByProject(String projectId) {
        List<Task> tasks = taskRepository.findByProject_Id(projectId);
        return mapperUtils.mapList(tasks, TaskDto.class);
    }

    // 🔹 Get tasks by user
    public List<TaskDto> getTasksByUser(String userId) {
        List<Task> tasks = taskRepository.findByAssignedTo_Id(userId);
        return mapperUtils.mapList(tasks, TaskDto.class);
    }

    // 🔹 Create task
    public TaskDto createTask(TaskDto taskDto) {
        Task task = mapperUtils.map(taskDto, Task.class);
        Task savedTask = taskRepository.save(task);
        return mapperUtils.map(savedTask, TaskDto.class);
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

        Task updated = taskRepository.save(task);
        return mapperUtils.map(updated, TaskDto.class);
    }

    // 🔹 Update only task status
    public TaskDto updateTaskStatus(String id, String status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(status);
        Task updated = taskRepository.save(task);
        return mapperUtils.map(updated, TaskDto.class);
    }

    public boolean deleteTask(String id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }
        taskRepository.deleteById(id);
        return true;
    }

}
