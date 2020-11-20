package com.example.fssupportcenter.Object;

public class ObjectProfileCenter {
    private String center_name, center_email,center_phone,center_type,center_address,
            center_latitude,center_longitude,center_city,center_status,center_id;

    public ObjectProfileCenter() {
    }

    public ObjectProfileCenter(String center_name, String center_email,
                               String center_phone, String center_type, String center_address,
                               String center_latitude, String center_longitude, String center_city, String center_status,String center_id) {
        this.center_name = center_name;
        this.center_email = center_email;
        this.center_phone = center_phone;
        this.center_type = center_type;
        this.center_address = center_address;
        this.center_latitude = center_latitude;
        this.center_longitude = center_longitude;
        this.center_city = center_city;
        this.center_status = center_status;
        this.center_id = center_id;
    }

    public String getCenter_id() {
        return center_id;
    }

    public void setCenter_id(String center_id) {
        this.center_id = center_id;
    }

    public String getCenter_status() {
        return center_status;
    }

    public void setCenter_status(String center_status) {
        this.center_status = center_status;
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

    public String getCenter_city() {
        return center_city;
    }

    public void setCenter_city(String center_city) {
        this.center_city = center_city;
    }

    public String getCenter_name() {
        return center_name;
    }

    public void setCenter_name(String center_name) {
        this.center_name = center_name;
    }

    public String getCenter_email() {
        return center_email;
    }

    public void setCenter_email(String center_email) {
        this.center_email = center_email;
    }

    public String getCenter_phone() {
        return center_phone;
    }

    public void setCenter_phone(String center_phone) {
        this.center_phone = center_phone;
    }

    public String getCenter_type() {
        return center_type;
    }

    public void setCenter_type(String center_type) {
        this.center_type = center_type;
    }

    public String getCenter_address() {
        return center_address;
    }

    public void setCenter_address(String center_address) {
        this.center_address = center_address;
    }
}