package com.example.project_management.model;

import java.io.Serializable;
import java.util.Objects;

public class UserProjectId implements Serializable {
    private Integer user;
    private Integer project;

    // Constructor mặc định
    public UserProjectId() {
    }

    // Constructor với các trường
    public UserProjectId(Integer user, Integer project) {
        this.user = user;
        this.project = project;
    }

    // Getters
    public Integer getUser() {
        return user;
    }

    public Integer getProject() {
        return project;
    }

    // Setters
    public void setUser(Integer user) {
        this.user = user;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    // equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProjectId that = (UserProjectId) o;
        return Objects.equals(user, that.user) && Objects.equals(project, that.project);
    }

    // hashCode
    @Override
    public int hashCode() {
        return Objects.hash(user, project);
    }
}