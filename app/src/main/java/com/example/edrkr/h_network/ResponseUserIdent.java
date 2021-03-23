package com.example.edrkr.h_network;


import com.google.gson.JsonArray;



public class ResponseUserIdent {

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPhoneNum() {
        return UserPhoneNum;
    }

    public void setUserPhoneNum(String userPhoneNum) {
        UserPhoneNum = userPhoneNum;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getFarmNum() {
        return farmCnt;
    }

    public void setFarmNum(String farmNum) {
        this.farmCnt = farmNum;
    }

    public JsonArray getFarmID() {
        return farmId;
    }

    public void setFarmID(JsonArray farmID) {
        this.farmId = farmID;
    }

    public JsonArray getFarmName() {
        return farmName;
    }

    public void setFarmName(JsonArray farmName) {
        this.farmName = farmName;
    }


    public String getUserNickName() { return UserNickName; }
    public void setUserNickName(String userNickName) { UserNickName = userNickName; }
    public String getUserIdent() { return UserIdent; }
    public void setUserIdent(String userIdent) { UserIdent = userIdent; }


    private String UserName;
    private String UserPhoneNum;
    private String UserEmail;
    private String farmCnt;
    private JsonArray farmId;
    private JsonArray farmName;


    private String UserNickName; //닉네임
    private String UserIdent; //유저 고유의 번호


}
