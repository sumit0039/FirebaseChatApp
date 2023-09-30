package com.encureit.firebasechatapp.model;

import com.google.firebase.Timestamp;

public class UserModel {

    private String phone;
    private String userName;
    private Timestamp createdTimeStamp;
    private String userId;

    public UserModel() {
    }

    public UserModel(String phone, String userName, Timestamp createdTimeStamp,String userId) {
        this.phone = phone;
        this.userName = userName;
        this.createdTimeStamp = createdTimeStamp;
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(Timestamp createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
