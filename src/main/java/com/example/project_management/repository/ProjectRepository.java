package com.example.project_management.repository;
import com.example.project_management.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProjectRepository extends JpaRepository<Project, Integer> {

}
