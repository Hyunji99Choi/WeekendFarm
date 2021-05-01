package com.example.edrkr.a_Network.Class.manager;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetUserEachFarm {
    @SerializedName("UserIdent")
    private int userid;

    @SerializedName("UserName")
    private String username;

    @SerializedName("UserPhoneNum")
    private String phonenum;

    @SerializedName("FarmID")
    private ArrayList<Integer> farmid;

    @SerializedName("FarmName")
    private ArrayList<String> farname;

    GetUserEachFarm(int id, String username, String phonenum, ArrayList<Integer> farmid, ArrayList<String> farmname){
        this.userid = id;
        this.username = username;
        this.phonenum = phonenum;
        this.farmid = farmid;
        this.farname = farmname;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public ArrayList<Integer> getFarmid() {
        return farmid;
    }

    public void setFarmid(ArrayList<Integer> farmid) {
        this.farmid = farmid;
    }

    public ArrayList<String> getFarname() {
        return farname;
    }

    public void setFarname(ArrayList<String> farname) {
        this.farname = farname;
    }

}
