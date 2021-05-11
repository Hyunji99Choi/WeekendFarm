package com.example.edrkr.a_Network.Class.manager;

import com.google.gson.annotations.SerializedName;

public class GetAllFarm {
    @SerializedName("FarmID")
    private int farmid;

    @SerializedName("FarmName")
    private String farmname;

    GetAllFarm(int id, String name){
        this.farmid = id;
        this.farmname = name;
    }

    public int getFarmid() {
        return farmid;
    }

    public void setFarmid(int farmid) {
        this.farmid = farmid;
    }

    public String getFarmname() {
        return farmname;
    }

    public void setFarmname(String farmname) {
        this.farmname = farmname;
    }

}
