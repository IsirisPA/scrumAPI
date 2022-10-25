package gr.vgs.mongo.service.impl;

import gr.vgs.mongo.entity.Project;
import gr.vgs.mongo.entity.User;
import gr.vgs.mongo.enums.EProjectRole;
import gr.vgs.mongo.model.ProjectModel;
import gr.vgs.mongo.model.ProjectRequest;
import gr.vgs.mongo.model.ProjectResponse;
import gr.vgs.mongo.model.UserModel;
import gr.vgs.mongo.repository.ProjectRepository;
import gr.vgs.mongo.repository.UserRepository;
import gr.vgs.mongo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public String addProject(ProjectRequest projectRequest) {
        try {

            Project newProject = new Project();
            newProject.setTitle(projectRequest.getTitle());
            newProject.setDescription(projectRequest.getDescription());
            newProject.setCreateDate(new Date());
            Set<User> users = new HashSet<>();

            if (projectRequest.getUsers() != null) {

                for (Map.Entry<String, String> entry : projectRequest.getUsers().entrySet()) {
                    Set<Project> projects = new HashSet<>();
                    User user = userRepository.findByUsername(entry.getKey()).orElseThrow(() -> new RuntimeException("Error: User is not found."));
                    projects = (user.getProjects() == null) ? new HashSet<Project>() : user.getProjects();
                    projectRepository.save(newProject);

                    projects.add(newProject);
                    user.setProjects(projects);
                    switch (entry.getValue()) {
                        case "Admin":
                            user.seteProjectRole(EProjectRole.ADMIN);
                            break;
                        case "Dev":
                            user.seteProjectRole(EProjectRole.DEVELOPER);
                            break;
                    }
                    userRepository.save(user);
                    // add user role for this project no need to save for the user
                    //UserModel userModel = new UserModel();
                    //userModel.setName(user.getUsername());

                    users.add(user);
                }
            }
            newProject.setUserSet(users);
            projectRepository.save(newProject);
            return "Project created successfully";
        } catch (Exception e) {
            return "Project creation failed";
        }
    }


    @Override
    public String editProject(ProjectRequest projectRequest) {
        try {


            Project project = projectRepository.findById(projectRequest.getId()).orElseThrow(() -> new RuntimeException("Error: Project is not found."));

            if (projectRequest.getTitle() != null) project.setTitle(projectRequest.getTitle());
            if (projectRequest.getDescription() != null) project.setDescription(projectRequest.getDescription());
            Set<User> users = new HashSet<>();
            Set<String> usersNew = projectRequest.getUsers().keySet();
            Set<String> usersOld = project.getUsers().stream().map(userModel -> userModel.getName()).collect(Collectors.toSet());
            usersOld.removeAll(usersNew);
            List<String> list = usersOld.stream().collect(Collectors.toList());
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    User usertest = userRepository.findByUsername(list.get(i)).orElseThrow();
                    usertest.getProjects().remove(project);

                }
            }
            if (projectRequest.getUsers() != null) {
                // remove those that doesn't exist


                for (Map.Entry<String, String> entry : projectRequest.getUsers().entrySet()) {


                    User user = userRepository.findByUsername(entry.getKey()).orElseThrow(() -> new RuntimeException("Error: User is not found."));
                    // if edited project exists already at the user don'w write it twice
                    Set<Project> projects = (user.getProjects() == null) ? new HashSet<Project>() : user.getProjects();
                    boolean exists = false;
                    for (Project project1 : projects) {
                        if (project.getId().equals(project.getId())) {
                            exists = true;
                        }

                    }
                    if (!exists) projects.add(project);
                    user.setProjects(projects);
                    projectRepository.save(project);
                    switch (entry.getValue()) {
                        case "Admin":
                            user.seteProjectRole(EProjectRole.ADMIN);
                            break;
                        case "Dev":
                            user.seteProjectRole(EProjectRole.DEVELOPER);
                            break;
                    }
                    userRepository.save(user);

                    users.add(user);
                }
            }


            project.setUserSet(users);
            if (projectRequest.getEndDate() != null) project.setEndDate(projectRequest.getEndDate());
            projectRepository.save(project);
            return "Projected edited successfully";
        } catch (Exception e) {
            return "Projected edited failed";
        }

    }

    @Override
    public ProjectRequest getProject(String projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Error: Project is not found."));
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setId(project.getId());
        projectRequest.setTitle(project.getTitle());
        projectRequest.setDescription(project.getDescription());
        Map<String, String> users = new HashMap<>();
        project.getUserSet().forEach(user -> {
            String role = "";
            if (user.geteProjectRole() != null) {
                switch (user.geteProjectRole()) {
                    case ADMIN:
                        role = "Admin";
                        break;
                    case DEVELOPER:
                        role = "Developer";
                        break;
                }

            }
            users.put(user.getUsername(),role);
        });
        projectRequest.setUsers(users);
        projectRequest.setStartDate(project.getCreateDate());
        projectRequest.setEndDate(project.getEndDate());
        return projectRequest;
    }

    @Override
    public String deleteProject(String projectId) {
        try {
            return "Project deleted successfully";
        } catch (Exception e) {
            return "Project delete failed";
        }
    }

    @Override
    public List<ProjectResponse> getAll() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectResponse> list = new ArrayList<>();
        ProjectResponse projectResponse = new ProjectResponse();
        if (projects != null) {
            for (Project project: projects) {
                projectResponse.setProjectId(project.getId());
                projectResponse.setTitle(project.getTitle());
                list.add(projectResponse);
            }
        }
        return list;
    }

    @Override
    public List<ProjectResponse> getUserProjects(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Error: User is not found."));
        List<ProjectResponse> list = new ArrayList<>();
        if (user.getProjects() != null) user.getProjects().forEach(project -> {
            ProjectResponse projectResponse = new ProjectResponse();
            projectResponse.setProjectId(project.getId());
            projectResponse.setTitle(project.getTitle());
            list.add(projectResponse);
        });
        return list;
    }

    @Override
    public boolean isUserProjectOrUserAdmin(String username, String projectId) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));
        if (user.geteProjectRole() != null) {
            if (user.geteProjectRole().equals(EProjectRole.ADMIN)) return true;
        }

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found!"));
        //project.getUserSet().stream().forEach(user1 -> System.out.println(user1.getUsername()));
        if (project.getUserSet().stream().anyMatch(user1 -> user1.getUsername().equals(username))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String addUserToProject(String projectId, String userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
            Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found!"));
            user.getProjects().add(project);
            projectRepository.save(project);
            userRepository.save(user);
            project.getUserSet().add(user);
            projectRepository.save(project);
            return "User was added to project successfully";
        } catch (Exception e) {
            return "User was not added to project";
        }

    }

    @Override
    public String removeUserFromProject(String projectId, String userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
            Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found!"));
            user.getProjects().removeIf(project1 -> project1.getId().equals(project.getId()));
            userRepository.save(user);
            project.getUserSet().removeIf(user1 -> user1.getUsername().equals(user.getUsername()));
            projectRepository.save(project);
            return "User was removed from project successfully";
        } catch (Exception e) {
            return "User was not removed from project";
        }

    }
}
