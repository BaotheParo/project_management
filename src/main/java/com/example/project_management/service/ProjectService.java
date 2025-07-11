package com.example.project_management.service;

import com.example.project_management.model.Project;
import com.example.project_management.model.User;
import com.example.project_management.model.UserProject;
import com.example.project_management.model.UserProjectId;
import com.example.project_management.repository.ProjectRepository;
import com.example.project_management.repository.UserProjectRepository;
import com.example.project_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    public List<Project> getAllProjects() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        return projectRepository.findAll();
    }

    public Project getProjectById(Integer id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        UserProject userProject = userProjectRepository.findById(new UserProjectId(user.getUserId(), id))
                .orElseThrow(() -> new IllegalArgumentException("User not authorized for project ID: " + id));
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + id));
    }

    public Project createProject(Project project) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        if (!user.getRoles().stream().anyMatch(role -> role.getRoleName().equals("ADMIN") || role.getRoleName().equals("MANAGER"))) {
            throw new IllegalArgumentException("User does not have permission to create projects");
        }
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public Project updateProject(Integer id, Project projectDetails) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        UserProject userProject = userProjectRepository.findById(new UserProjectId(user.getUserId(), id))
                .orElseThrow(() -> new IllegalArgumentException("User not authorized for project ID: " + id));
        if (!userProject.getRole().getRoleName().equals("ADMIN") && !userProject.getRole().getRoleName().equals("MANAGER")) {
            throw new IllegalArgumentException("User does not have permission to update this project");
        }
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + id));
        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());
        project.setStartDate(projectDetails.getStartDate());
        project.setEndDate(projectDetails.getEndDate());
        project.setStatus(projectDetails.getStatus());
        project.setUpdatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public void deleteProject(Integer id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        UserProject userProject = userProjectRepository.findById(new UserProjectId(user.getUserId(), id))
                .orElseThrow(() -> new IllegalArgumentException("User not authorized for project ID: " + id));
        if (!userProject.getRole().getRoleName().equals("ADMIN")) {
            throw new IllegalArgumentException("User does not have permission to delete this project");
        }
        projectRepository.deleteById(id);
    }
}