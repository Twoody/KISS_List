package com.beWoody.tanner.KISS_List;

public class Categories {
    private String category;
    private int place;
    private int id;
    public Categories() {
        this.category = null;
        this.place    = 0;
    }
    public Categories(String category, int place) {
        super();
        this.category = category;
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
}
