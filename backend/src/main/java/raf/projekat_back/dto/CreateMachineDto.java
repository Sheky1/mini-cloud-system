package raf.projekat_back.dto;

import raf.projekat_back.model.User;

public class CreateMachineDto {

    private String name;
    private User createdBy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
