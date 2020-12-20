package com.facugarbino.spacexteam.models;

public enum TaskCategory {
    MAINTENANCE("Maintenance", "5fde9b5f86c6bc9cc5f8f2e7"),
    RESEARCH("Research", "5fde9b5f86c6bc9cc5f8f2eb"),
    TEST("Test", "5fde9b5f86c6bc9cc5f8f2ec");

    private final String actualLabel;
    private final String id;

    TaskCategory(String actualLabel, String id) {
        this.actualLabel = actualLabel;
        this.id = id;
    }

    public String getActualLabel() {
        return actualLabel;
    }

    public String getId() {
        return id;
    }

    public static TaskCategory getFromLabel(String label) {
        for (TaskCategory value : TaskCategory.values()) {
            if (value.getActualLabel().equals(label)) {
                return value;
            }
        }
        return null;
    }
}
