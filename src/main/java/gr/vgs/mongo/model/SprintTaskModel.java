package gr.vgs.mongo.model;

import gr.vgs.mongo.enums.ETaskStatus;

public class SprintTaskModel {
    String id;
    String title;
    ETaskStatus status;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ETaskStatus getStatus() {
        return status;
    }

    public SprintTaskModel() {
    }

    public SprintTaskModel(String id, String title, ETaskStatus status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }
}
