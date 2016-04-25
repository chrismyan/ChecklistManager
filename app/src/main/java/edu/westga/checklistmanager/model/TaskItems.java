package edu.westga.checklistmanager.model;

/**
 * Created by Chris Yan on 4/19/2016.
 */
public class TaskItems {
    private String name;
    private int id;
    private int eventId;

    public TaskItems() {

    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    private int completed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int categoryId) {
        this.eventId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
