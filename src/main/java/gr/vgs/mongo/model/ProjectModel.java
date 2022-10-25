package gr.vgs.mongo.model;

public class ProjectModel {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProjectModel(String id) {
        this.id = id;
    }

    public ProjectModel() {
    }
}
