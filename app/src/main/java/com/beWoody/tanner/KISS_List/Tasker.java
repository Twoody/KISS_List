package com.beWoody.tanner.KISS_List;

public class Tasker {
    private String category;
    private String content;
    private int status;
    private int place;
    private int id;
    private int catId;
    public Tasker() {
        this.category = null; //1
        this.content  = null; //2
        this.status   = 0;    //3
        this.place    = 0;    //4
        this.catId    = 0;    //5
    }
    public Tasker(String category, String content, int status, int place, int catId) {
        super();
        this.category = category;
        this.content  = content;
        this.status   = status;
        this.place    = place;
        this.catId    = catId;
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
    public int getCatId() {
        return catId;
    }
    public void setCatId(int status) {
        this.catId = catId;
    }
}
