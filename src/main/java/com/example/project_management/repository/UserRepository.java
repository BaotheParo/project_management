package com.example.project_management.repository;
import com.example.project_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
