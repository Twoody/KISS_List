package com.beWoody.tanner.KISS_List;

public class AvailableColors {
    private String colorname;
    private String hex;
    private int color;
    private int id;
    public AvailableColors() {
        this.colorname  = null;
        this.hex        = null;
    }
    public AvailableColors(String colorname, String hex) {
        super();
        this.colorname = colorname;
        this.hex       = hex;
        //this.color     = (int) Long.parseLong(hex, 16);
    }

    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getColorname() {
        return colorname;
    }
    public void setColorname(String colorname) {
        this.colorname = colorname;
    }
    public String getHex() {
        return hex;
    }
    public void setHex(String hex) {
        this.hex = hex;
    }
}
