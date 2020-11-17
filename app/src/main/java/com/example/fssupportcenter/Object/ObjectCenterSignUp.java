package com.example.fssupportcenter.Object;

public class ObjectCenterSignUp {
    private String centerName, presenter,centerEmail,centerAddress,centerPhone,centerType;

    public ObjectCenterSignUp() {
    }

    public ObjectCenterSignUp(String centerName, String centerEmail, String centerPhone) {
        this.centerName = centerName;
        this.centerEmail = centerEmail;
        this.centerPhone = centerPhone;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getCenterEmail() {
        return centerEmail;
    }

    public void setCenterEmail(String centerEmail) {
        this.centerEmail = centerEmail;
    }

    public String getCenterPhone() {
        return centerPhone;
    }

    public void setCenterPhone(String centerPhone) {
        this.centerPhone = centerPhone;
    }

}
