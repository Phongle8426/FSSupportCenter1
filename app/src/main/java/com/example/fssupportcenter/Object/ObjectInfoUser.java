package com.example.fssupportcenter.Object;

public class ObjectInfoUser {
    public String name,birthday, email, phone, address, idcard, blood, note;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public ObjectInfoUser() {
    }



    public ObjectInfoUser(String name, String email, String phone, String address, String IDcard, String blood, String note, String birthday) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.idcard = IDcard;
        this.blood = blood;
        this.note = note;
        this.birthday = birthday;
    }
}
