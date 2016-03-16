package com.lockerhops.backend;

/**
 * The object model for the data we are sending through endpoints
 */
public class MyBean {

    private String myData;

    private Boolean myBoolean;

    public Boolean getMyBoolean() { return myBoolean; }

    public void setMyBoolean(Boolean set) { myBoolean = set; }

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }
}