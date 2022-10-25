package gr.vgs.mongo.model;


import gr.vgs.mongo.enums.EProjectRole;

public class UserModel {
    private String name;
    private EProjectRole eProjectRole;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EProjectRole geteProjectRole() {
        return eProjectRole;
    }

    public void seteProjectRole(EProjectRole eProjectRole) {
        this.eProjectRole = eProjectRole;
    }
}
