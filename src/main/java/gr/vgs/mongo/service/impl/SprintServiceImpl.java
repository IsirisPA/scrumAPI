package gr.vgs.mongo.service.impl;

import gr.vgs.mongo.entity.Sprint;
import gr.vgs.mongo.entity.Task;
import gr.vgs.mongo.enums.ESprintStatus;
import gr.vgs.mongo.model.SprintRequest;
import gr.vgs.mongo.model.SprintResponse;
import gr.vgs.mongo.model.SprintTaskModel;
import gr.vgs.mongo.model.TaskModel;
import gr.vgs.mongo.repository.SprintRepository;
import gr.vgs.mongo.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SprintServiceImpl implements SprintService {
    @Autowired
    SprintRepository sprintRepository;

    @Override
    public String addSprint(SprintRequest sprintRequest) {
        try {
            Sprint sprint = new Sprint();
            sprint.setTitle(sprintRequest.getTitle());
            sprint.setDescription(sprintRequest.getDescription());
            sprint.setProjectId(sprintRequest.getProjectId());
            sprint.setCreateDate(sprintRequest.getCreateDate());
            sprint.setEndDate(sprintRequest.getEndDate());
            sprint.seteSprintStatus(ESprintStatus.IN_PROGRESS);
            sprintRepository.save(sprint);
            return "Sprint added successfully";
        } catch (Exception e) {
            return "Sprint was not added";
        }
    }

    @Override
    public String editSprint(SprintRequest sprintRequest) {
        try {
            Sprint sprint = sprintRepository.findById(sprintRequest.getId()).orElseThrow(() -> new RuntimeException("Sprint not found"));
            if (sprintRequest.getTitle() != null) sprint.setTitle(sprintRequest.getTitle());
            if (sprintRequest.getDescription() != null)sprint.setDescription(sprintRequest.getDescription());
            if (sprintRequest.getCreateDate() != null)sprint.setCreateDate(sprintRequest.getCreateDate());
            if (sprintRequest.getEndDate() != null)sprint.setEndDate(sprintRequest.getEndDate());
            if (sprintRequest.getStatus() != null) {
                switch (sprintRequest.getStatus()) {
                    case "inProgress": sprint.seteSprintStatus(ESprintStatus.IN_PROGRESS);
                    case "completed": sprint.seteSprintStatus(ESprintStatus.COMPLETED);
                }
            }
            sprintRepository.save(sprint);
            return "Sprint edit successfully";
        } catch (Exception e) {
            return "Sprint edit failed";
        }


    }

    @Override
    public void removeSprint(String id) {

    }

    @Override
    public SprintRequest getSprint(String id) {
        SprintRequest sprintRequest = new SprintRequest();
        Sprint sprint = sprintRepository.findById(id).orElseThrow(() -> new RuntimeException("Sprint not found"));
        sprintRequest.setId(sprint.getId());
        sprintRequest.setTitle(sprint.getTitle());
        sprintRequest.setDescription(sprint.getDescription());
        sprintRequest.setProjectId(sprint.getProjectId());
        sprintRequest.setCreateDate(sprint.getCreateDate());
        sprintRequest.setEndDate(sprint.getEndDate());
        switch (sprint.geteSprintStatus()) {
            case IN_PROGRESS:
                sprintRequest.setStatus("inProgress");
                break;
            case COMPLETED:
                sprintRequest.setStatus("completed");
                break;
        }

        Set<SprintTaskModel> tasks = new HashSet<>();
        sprint.getTasks().stream().forEach(task -> tasks.add(new SprintTaskModel(task.getId(), task.getTitle(), task.geteTaskStatus())));
        sprintRequest.setTasks(tasks);
        return sprintRequest;
    }

    @Override
    public List<SprintResponse> getProjectSprints(String projectId) {
        List<Sprint> sprints = sprintRepository.findAllByProjectId(projectId);
        List<SprintResponse> responses = new ArrayList<>();
        SprintResponse sprintResponse;
        if (sprints != null) {
            for (Sprint sprint:sprints) {
                sprintResponse = new SprintResponse();
                sprintResponse.setSprintId(sprint.getId());
                sprintResponse.setTitle(sprint.getTitle());
                switch (sprint.geteSprintStatus()) {
                    case IN_PROGRESS:
                        sprintResponse.setStatus("inProgress");
                        break;
                    case COMPLETED:
                        sprintResponse.setStatus("completed");
                        break;
                }
                responses.add(sprintResponse);

            }
        }
        return responses;
    }

    @Override
    public String editTaskToSprint(String sprintId, Task task, String action) {
        try {
            Sprint sprint = sprintRepository.findById(sprintId).orElseThrow(() -> new RuntimeException("Sprint not found"));

            switch (action) {
                case "add":
                    sprint.getTasks().add(task);
                    sprintRepository.save(sprint);
                    break;
                case "delete":
                    sprint.getTasks().remove(task);
                    sprintRepository.save(sprint);
                    break;
            }
            return "Task edited successfully ";
        } catch (Exception e) {
            return "Task edit failed";
        }

    }
}
