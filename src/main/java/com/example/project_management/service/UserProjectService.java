package com.example.project_management.service;

import com.example.project_management.model.UserProject;
import com.example.project_management.model.UserProjectId;
import com.example.project_management.repository.ProjectRepository;
import com.example.project_management.repository.RoleRepository;
import com.example.project_management.repository.UserProjectRepository;
import com.example.project_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserProjectService {

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserProject assignUserToProject(UserProject userProject) {
        // Kiểm tra xem user, project, và role có tồn tại không
        if (!userRepository.existsById(userProject.getUser().getUserId())) {
            throw new IllegalArgumentException("User not found with ID: " + userProject.getUser().getUserId());
        }
        if (!projectRepository.existsById(userProject.getProject().getProjectId())) {
            throw new IllegalArgumentException("Project not found with ID: " + userProject.getProject().getProjectId());
        }
        if (!roleRepository.existsById(userProject.getRole().getRoleId())) {
            throw new IllegalArgumentException("Role not found with ID: " + userProject.getRole().getRoleId());
        }

        // Thiết lập thời gian gán
        userProject.setAssignedAt(LocalDateTime.now());
        return userProjectRepository.save(userProject);
    }

    public void removeUserFromProject(Integer userId, Integer projectId) {
        UserProjectId id = new UserProjectId(userId, projectId);
        if (!userProjectRepository.existsById(id)) {
            throw new IllegalArgumentException("User with ID " + userId + " is not assigned to project with ID " + projectId);
        }
        userProjectRepository.deleteById(id);
    }

    public List<UserProject> getUsersByProject(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new IllegalArgumentException("Project not found with ID: " + projectId);
        }
        return userProjectRepository.findByProjectProjectId(projectId);
    }

    public List<UserProject> getProjectsByUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        return userProjectRepository.findByUserUserId(userId);
    }
}