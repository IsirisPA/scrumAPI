package gr.vgs.mongo.entity;

import gr.vgs.mongo.enums.ETaskStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.sql.Timestamp;

@Document(collection = "tasks")
public class Task {
    @Id
    private String id;

    @NotBlank
    @Size(max = 20)
    private String title;

    @Size(max = 200)
    private String Description;


    private String assignedUserId;


    private String creatorUserId;

    @NotBlank
    private String sprintId;

    @NotBlank
    @CreatedDate
    private Date createDate;


    private Date completedDate;

    @NotBlank
    private ETaskStatus eTaskStatus;

    @NotBlank
    private String taskType;

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

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getSprintId() {
        return sprintId;
    }

    public void setSprintId(String sprintId) {
        this.sprintId = sprintId;
    }

    public ETaskStatus geteTaskStatus() {
        return eTaskStatus;
    }

    public void seteTaskStatus(ETaskStatus eTaskStatus) {
        this.eTaskStatus = eTaskStatus;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}
