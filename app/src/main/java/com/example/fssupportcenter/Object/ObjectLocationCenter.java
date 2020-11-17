package com.example.fssupportcenter.Object;

public class ObjectLocationCenter {
    String latitude,longitude,city,namecenter,centerid;

    public ObjectLocationCenter() {
    }

    public ObjectLocationCenter(String latitude, String longitude,String city,String namecenter,String centerid) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.namecenter = namecenter;
        this.centerid = centerid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNamecenter() {
        return namecenter;
    }

    public void setNamecenter(String namecenter) {
        this.namecenter = namecenter;
    }

    public String getCenterid() {
        return centerid;
    }

    public void setCenterid(String centerid) {
        this.centerid = centerid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
