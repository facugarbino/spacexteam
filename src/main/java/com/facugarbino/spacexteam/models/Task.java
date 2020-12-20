package com.facugarbino.spacexteam.models;

public class Task extends Card {

    private TaskCategory category;

    public Task() {

    }

    public Task(String title, TaskCategory category) {
        this.title = title;
        this.category = category;
    }

    public TaskCategory getCategory() {
        return category;
    }

    public void setCategory(TaskCategory category) {
        this.category = category;
    }
}
