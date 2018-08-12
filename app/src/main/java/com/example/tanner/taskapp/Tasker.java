package com.example.tanner.taskapp;

public class Tasker {
    private String category;
    private String content;
    private int status;
    private int place;
    private int id;
    public Tasker() {
        this.category = null; //1
        this.content  = null; //2
        this.status   = 0;    //3
        this.place    = 0;    //4
    }
    public Tasker(String category, String content, int status, int place) {
        super();
        this.category = category;
        this.content  = content;
        this.status   = status;
        this.place    = place;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getPlace() {
        return place;
    }
    public int setPlace(int place) {
        return this.place = place;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}
