package com.beWoody.tanner.KISS_List;

public class User {
    private String font;
    private int colorPrimary;
    private int colorSecondary;
    private int fontcolor;
    private int fontsize;
    private int isAppending; //Boolean
    private int listcolor;
    private int id;
    public User() {
        this.font           = null; //1
        this.colorPrimary   = 0; //2
        this.colorSecondary = 0; //3
        this.listcolor      = 0; //4
        this.fontcolor      = 0; //5
        this.fontsize       = 0; //6
        this.isAppending    = 0; //7
    }
    public User(String font, int colorPrimary, int colorSecondary, int listcolor, int fontcolor, int fontsize, int isAppending) {
        super();
        this.font           = font;
        this.colorPrimary   = colorPrimary;
        this.colorSecondary = colorSecondary;
        this.listcolor      = listcolor;
        this.fontcolor      = fontcolor;
        this.fontsize       = fontsize;
        this.isAppending    = isAppending;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setListcolor(int listcolor){
        this.listcolor = listcolor;
    }
    public int getListcolor(){return listcolor;}
    public int getIsAppending(){
        return isAppending;
    }
    public int setIsAppending(int isAppending){
        return this.isAppending = isAppending;
    }
    public int getFontcolor() {
        return fontcolor;
    }
    public int setFontcolor(int fontcolor){
        return this.fontcolor = fontcolor;
    }
    public int getFontsize() {
        return fontsize;
    }
    public int setFontsize(int fontsize) {
        return this.fontsize = fontsize;
    }
    public int getColorPrimary() {
        return colorPrimary;
    }
    public void setColorPrimary(int colorPrimary) {
        this.colorPrimary = colorPrimary;
    }
    public int getColorSecondary() {
        return colorSecondary;
    }
    public void setColorSecondary(int colorSecondary){
        this.colorSecondary = colorSecondary;
    }
    public String getFont() {
        return font;
    }
    public void setFont(String font) {
        this.font = font;
    }
}
