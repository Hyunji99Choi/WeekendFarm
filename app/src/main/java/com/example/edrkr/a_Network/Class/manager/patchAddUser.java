package com.example.edrkr.a_Network.Class.manager;

import com.example.edrkr.a_Network.Class.Post;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class patchAddUser implements Post {
    @SerializedName("UserIdent")
    private ArrayList<Integer> userid;

    public patchAddUser(ArrayList<Integer> userid){
        this.userid = userid;
    }

    public ArrayList<Integer> getUserid() {
        return userid;
    }

    public void setUserid(ArrayList<Integer> userid) {
        this.userid = userid;
    }
}
