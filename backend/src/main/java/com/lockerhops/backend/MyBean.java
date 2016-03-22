package com.lockerhops.backend;

/**
 * The object model for the data we are sending through endpoints
 */
public class MyBean {

    private String myData;

    private Boolean myBoolean;

    private int myInt;

    public int getMyInt() { return myInt; }

    public void setMyInt(int set) { myInt = set; }

    public Boolean getMyBoolean() { return myBoolean; }

    public void setMyBoolean(Boolean set) { myBoolean = set; }

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }
}