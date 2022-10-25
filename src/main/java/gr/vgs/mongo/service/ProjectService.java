package gr.vgs.mongo.service;

import gr.vgs.mongo.entity.Project;
import gr.vgs.mongo.model.ProjectRequest;
import gr.vgs.mongo.model.ProjectResponse;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    String addProject(ProjectRequest projectRequest);
    String editProject(ProjectRequest projectRequest);
    ProjectRequest getProject(String projectId);
    String deleteProject(String projectId);
    List<ProjectResponse> getAll();
    List<ProjectResponse> getUserProjects(String userId);
    boolean isUserProjectOrUserAdmin(String username, String projectId);
    String addUserToProject(String projectId, String userId);
    String removeUserFromProject(String projectId, String userId);
}
