package com.example.project_management.service;

import com.example.project_management.model.Task;
import com.example.project_management.model.User;
import com.example.project_management.model.UserProject;
import com.example.project_management.model.UserProjectId;
import com.example.project_management.repository.ProjectRepository;
import com.example.project_management.repository.TaskRepository;
import com.example.project_management.repository.UserProjectRepository;
import com.example.project_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    public List<Task> getTasksByProjectId(Integer projectId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        UserProject userProject = userProjectRepository.findById(new UserProjectId(user.getUserId(), projectId))
                .orElseThrow(() -> new IllegalArgumentException("User not authorized for project ID: " + projectId));
        return taskRepository.findByProjectProjectId(projectId);
    }

    public Task createTask(Task task) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        UserProject userProject = userProjectRepository.findById(new UserProjectId(user.getUserId(), task.getProject().getProjectId()))
                .orElseThrow(() -> new IllegalArgumentException("User not authorized for project ID: " + task.getProject().getProjectId()));
        if (!userProject.getRole().getRoleName().equals("ADMIN") && !userProject.getRole().getRoleName().equals("MANAGER")) {
            throw new IllegalArgumentException("User does not have permission to create tasks");
        }
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task updateTask(Integer id, Task taskDetails) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
        UserProject userProject = userProjectRepository.findById(new UserProjectId(user.getUserId(), task.getProject().getProjectId()))
                .orElseThrow(() -> new IllegalArgumentException("User not authorized for project ID: " + task.getProject().getProjectId()));
        if (!userProject.getRole().getRoleName().equals("ADMIN") && !userProject.getRole().getRoleName().equals("MANAGER")) {
            throw new IllegalArgumentException("User does not have permission to update tasks");
        }
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setAssignedUser(taskDetails.getAssignedUser());
        task.setPriority(taskDetails.getPriority());
        task.setStatus(taskDetails.getStatus());
        task.setDueDate(taskDetails.getDueDate());
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public void deleteTask(Integer id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
        UserProject userProject = userProjectRepository.findById(new UserProjectId(user.getUserId(), task.getProject().getProjectId()))
                .orElseThrow(() -> new IllegalArgumentException("User not authorized for project ID: " + task.getProject().getProjectId()));
        if (!userProject.getRole().getRoleName().equals("ADMIN")) {
            throw new IllegalArgumentException("User does not have permission to delete tasks");
        }
        taskRepository.deleteById(id);
    }
}