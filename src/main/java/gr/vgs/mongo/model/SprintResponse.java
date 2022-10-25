package gr.vgs.mongo.model;

import java.util.HashSet;
import java.util.Set;

public class SprintResponse {
    String sprintId;
    String title;
    String status;

    Set<TaskModel> tasks = new HashSet<>();

    public String getSprintId() {
        return sprintId;
    }

    public void setSprintId(String sprintId) {
        this.sprintId = sprintId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<TaskModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskModel> tasks) {
        this.tasks = tasks;
    }
}
