package com.example.fssupportcenter.Object;

public class ObjectHistoryTeam {
    private String time,user_id,user_name,user_latitude,user_longitude;

    public ObjectHistoryTeam() {
    }

    public ObjectHistoryTeam(String time, String user_id, String user_name, String user_latitude, String user_longitude) {
        this.time = time;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_latitude = user_latitude;
        this.user_longitude = user_longitude;
    }

    public String getUser_latitude() {
        return user_latitude;
    }

    public void setUser_latitude(String user_latitude) {
        this.user_latitude = user_latitude;
    }

    public String getUser_longitude() {
        return user_longitude;
    }

    public void setUser_longitude(String user_longitude) {
        this.user_longitude = user_longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
