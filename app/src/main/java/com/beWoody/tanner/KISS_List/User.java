package com.beWoody.tanner.KISS_List;

public class User {
    private String font;
    private String colorPrimary;
    private String colorSecondary;
    private String fontcolor;
    private int fontsize;
    private int id;
    public User() {
        this.font           = null; //1
        this.colorPrimary   = null; //2
        this.colorSecondary = null; //3
        this.fontcolor      = null; //4
        this.fontsize       = 0;    //5
    }
    public User(String font, String colorPrimary, String colorSecondary, String fontcolor, int fontsize) {
        super();
        this.font           = font;
        this.colorPrimary   = colorPrimary;
        this.colorSecondary = colorSecondary;
        this.fontcolor      = fontcolor;
        this.fontsize       = fontsize;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFontcolor() {
        return fontcolor;
    }
    public String setFontcolor(String fontcolor){
        return this.fontcolor = fontcolor;
    }
    public int getFontsize() {
        return fontsize;
    }
    public int setFontsize(int fontsize) {
        return this.fontsize = fontsize;
    }
    public String getColorPrimary() {
        return colorPrimary;
    }
    public void setColorPrimary(String colorPrimary) {
        this.colorPrimary = colorPrimary;
    }
    public String getColorSecondary() {
        return colorSecondary;
    }
    public void setColorSecondary(String colorSecondary){
        this.colorSecondary = colorSecondary;
    }
    public String getFont() {
        return font;
    }
    public void setFont(String font) {
        this.font = font;
    }
}
