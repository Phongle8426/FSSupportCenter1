package com.example.fssupportcenter.Object;

public class ObjectSupportCenter {
    public String center_id,center_latitude,center_longitude,center_name,center_status;

    public ObjectSupportCenter() {
    }

    public ObjectSupportCenter(String center_id, String center_latitude, String center_longitude, String center_name,String center_status) {
        this.center_id = center_id;
        this.center_latitude = center_latitude;
        this.center_longitude = center_longitude;
        this.center_name = center_name;
        this.center_status = center_status;
    }

    public String getCenter_status() {
        return center_status;
    }

    public void setCenter_status(String center_status) {
        this.center_status = center_status;
    }

    public String getCenter_id() {
        return center_id;
    }

    public void setCenter_id(String center_id) {
        this.center_id = center_id;
    }

    public String getCenter_latitude() {
        return center_latitude;
    }

    public void setCenter_latitude(String center_latitude) {
        this.center_latitude = center_latitude;
    }

    public String getCenter_longitude() {
        return center_longitude;
    }

    public void setCenter_longitude(String center_longitude) {
        this.center_longitude = center_longitude;
    }

    public String getCenter_name() {
        return center_name;
    }

    public void setCenter_name(String center_name) {
        this.center_name = center_name;
    }
}
