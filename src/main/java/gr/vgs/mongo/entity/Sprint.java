package gr.vgs.mongo.entity;

import gr.vgs.mongo.enums.ESprintStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "sprints")
public class Sprint {
    @Id
    private String id;

    @NotBlank
    @Size(max = 20)
    private String title;

    @Size(max = 200)
    private String Description;

    @NotBlank
    @CreatedDate
    private Date createDate;

    @NotBlank
    @CreatedDate
    private Date endDate;

    @NotBlank
    private String projectId;

    private ESprintStatus eSprintStatus;

    @DBRef(lazy = true)
    Set<Task> tasks = new HashSet<>();

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public ESprintStatus geteSprintStatus() {
        return eSprintStatus;
    }

    public void seteSprintStatus(ESprintStatus eSprintStatus) {
        this.eSprintStatus = eSprintStatus;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
