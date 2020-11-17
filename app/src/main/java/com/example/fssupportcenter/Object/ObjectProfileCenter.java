package com.example.fssupportcenter.Object;

public class ObjectProfileCenter {
    private String center_name, center_email,center_phone,center_type,center_address;

    public ObjectProfileCenter() {
    }

    public ObjectProfileCenter(String center_name, String center_email, String center_phone, String center_type, String center_address) {
        this.center_name = center_name;
        this.center_email = center_email;
        this.center_phone = center_phone;
        this.center_type = center_type;
        this.center_address = center_address;
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
