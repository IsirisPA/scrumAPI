package gr.vgs.mongo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SprintRequest {
    private String id;

    private String title;

    private String Description;

    private Date createDate;

    private Date endDate;

    private String projectId;

    private String status;

    Set<SprintTaskModel> tasks = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<SprintTaskModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<SprintTaskModel> tasks) {
        this.tasks = tasks;
    }
}
