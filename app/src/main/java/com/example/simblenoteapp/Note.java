package com.example.simblenoteapp;

public class Note {
    private String title, description;

    long time;
    String uid,id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Note(String title, String description, long time, String uid, String id) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.uid = uid;
        this.id = id;
    }

    public Note() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
