package com.example.project_management.repository;

import com.example.project_management.model.UserProject;
import com.example.project_management.model.UserProjectId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProject, UserProjectId> {
}