package com.facugarbino.spacexteam.models;

public class Issue extends Card {
    private String description;

    public Issue() {

    }

    public Issue(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
