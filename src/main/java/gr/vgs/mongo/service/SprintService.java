package gr.vgs.mongo.service;

import gr.vgs.mongo.entity.Task;
import gr.vgs.mongo.model.SprintRequest;
import gr.vgs.mongo.model.SprintResponse;

import java.util.List;

public interface SprintService {
    String addSprint(SprintRequest sprintRequest);
    String editSprint(SprintRequest sprintRequest);
    void removeSprint(String id);
    SprintRequest getSprint(String id);
    List<SprintResponse> getProjectSprints(String projectId);
    String editTaskToSprint(String sprintId, Task task, String action);
}
