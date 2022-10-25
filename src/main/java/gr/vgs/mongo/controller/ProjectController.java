package gr.vgs.mongo.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import gr.vgs.mongo.model.ProjectRequest;
import gr.vgs.mongo.model.ProjectResponse;
import gr.vgs.mongo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/project")

public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> addProject(@Valid @RequestBody ProjectRequest projectRequest) {
        String response = projectService.addProject(projectRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public ResponseEntity<?> previewProject(@Valid @RequestBody ObjectNode node) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username ="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        boolean isValidUser = projectService.isUserProjectOrUserAdmin(username, node.get("projectId").asText());
        ProjectRequest projectRequest;
        if (isValidUser)  {
             projectRequest = projectService.getProject(node.get("projectId").asText(""));
        } else {
            return ResponseEntity.ok("Project not available");
        }
        return ResponseEntity.ok(projectRequest);
//        ProjectRequest projectRequest = projectService.getProject(node.get("projectId").asText(""));
//        return ResponseEntity.ok(projectRequest);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<ProjectResponse>> previewAllProject() {
        List<ProjectResponse> list = projectService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/get/user")
    public ResponseEntity<List<ProjectResponse>> previewUserProject() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username ="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        List<ProjectResponse> list = projectService.getUserProjects(username);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> editProject(@Valid @RequestBody ProjectRequest projectRequest) {

        // fix the response
        String response = projectService.editProject(projectRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> addUserProject(@Valid @RequestBody ObjectNode node) {
        String response = projectService.addUserToProject(node.get("projectId").asText(), node.get("userId").asText());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> removeUserProject(@Valid @RequestBody ObjectNode node) {
        String response = projectService.removeUserFromProject(node.get("projectId").asText(), node.get("userId").asText());
        return ResponseEntity.ok(response);
    }


}
