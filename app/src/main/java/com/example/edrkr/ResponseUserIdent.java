package com.example.edrkr;


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
        return farmNum;
    }

    public void setFarmNum(String farmNum) {
        this.farmNum = farmNum;
    }

    public JsonArray getFarmID() {
        return farmID;
    }

    public void setFarmID(JsonArray farmID) {
        this.farmID = farmID;
    }

    public JsonArray getFarmName() {
        return farmName;
    }

    public void setFarmName(JsonArray farmName) {
        this.farmName = farmName;
    }



    private String UserName;
    private String UserPhoneNum;
    private String UserEmail;
    private String farmNum;
    private JsonArray farmID;
    private JsonArray farmName;


}
