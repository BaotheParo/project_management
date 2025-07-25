package com.example.project_management.repository;
import com.example.project_management.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByProjectProjectId(Integer projectId);
}
