package com.example.project_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_projects")
@IdClass(UserProjectId.class)
public class UserProject {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private LocalDateTime assignedAt;

    // Constructor mặc định
    public UserProject() {
    }

    // Constructor với các trường
    public UserProject(User user, Project project, Role role, LocalDateTime assignedAt) {
        this.user = user;
        this.project = project;
        this.role = role;
        this.assignedAt = assignedAt;
    }

    // Getters
    public User getUser() {
        return user;
    }

    public Project getProject() {
        return project;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    // Setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    // toString
    @Override
    public String toString() {
        return "UserProject{" +
                "user=" + user +
                ", project=" + project +
                ", role=" + role +
                ", assignedAt=" + assignedAt +
                '}';
    }

    // equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProject that = (UserProject) o;
        return user != null && user.equals(that.user) &&
                project != null && project.equals(that.project);
    }

    // hashCode
    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (project != null ? project.hashCode() : 0);
        return result;
    }
}