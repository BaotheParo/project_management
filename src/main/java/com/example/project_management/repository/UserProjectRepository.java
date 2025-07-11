package com.example.project_management.repository;

import com.example.project_management.model.UserProject;
import com.example.project_management.model.UserProjectId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProjectRepository extends JpaRepository<UserProject, UserProjectId> {
    List<UserProject> findByProjectProjectId(Integer projectId);
    List<UserProject> findByUserUserId(Integer userId);
}