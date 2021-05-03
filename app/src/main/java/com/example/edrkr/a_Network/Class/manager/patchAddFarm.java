package com.example.edrkr.a_Network.Class.manager;

import com.example.edrkr.a_Network.Class.Post;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class patchAddFarm implements Post {
    @SerializedName("UserIdent")
    private ArrayList<Integer> farmid;

    public patchAddFarm(ArrayList<Integer> userid){
        this.farmid = userid;
    }

    public ArrayList<Integer> getUserid() {
        return farmid;
    }

    public void setUserid(ArrayList<Integer> userid) {
        this.farmid = userid;
    }
}
