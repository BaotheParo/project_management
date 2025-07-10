package com.example.project_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_roles")
public class UserRole {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private LocalDateTime assignedAt;

    // Constructor mặc định
    public UserRole() {
    }

    // Constructor với các trường
    public UserRole(User user, Role role, LocalDateTime assignedAt) {
        this.user = user;
        this.role = role;
        this.assignedAt = assignedAt;
    }

    // Getters
    public User getUser() {
        return user;
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

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    // toString
    @Override
    public String toString() {
        return "UserRole{" +
                "user=" + user +
                ", role=" + role +
                ", assignedAt=" + assignedAt +
                '}';
    }

    // equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return user != null && user.equals(userRole.user) &&
                role != null && role.equals(userRole.role);
    }

    // hashCode
    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}