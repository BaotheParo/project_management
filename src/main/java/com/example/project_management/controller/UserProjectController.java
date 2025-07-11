package com.example.project_management.controller;

import com.example.project_management.model.UserProject;
import com.example.project_management.service.UserProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-projects")
public class UserProjectController {

    @Autowired
    private UserProjectService userProjectService;

    @PostMapping
    public ResponseEntity<UserProject> assignUserToProject(@RequestBody UserProject userProject) {
        UserProject savedUserProject = userProjectService.assignUserToProject(userProject);
        return ResponseEntity.ok(savedUserProject);
    }

    @DeleteMapping("/{userId}/{projectId}")
    public ResponseEntity<Void> removeUserFromProject(@PathVariable Integer userId, @PathVariable Integer projectId) {
        userProjectService.removeUserFromProject(userId, projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<UserProject>> getUsersByProject(@PathVariable Integer projectId) {
        return ResponseEntity.ok(userProjectService.getUsersByProject(projectId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserProject>> getProjectsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(userProjectService.getProjectsByUser(userId));
    }
}