package com.example.edrkr.a_Network.Class.manager;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class inputFarm {
    @SerializedName("inputFarm")
    int[] inputfarm;

    public int[] getInputfarm() {
        return inputfarm;
    }

    public void setInputfarm(int[] inputfarm) {
        this.inputfarm = inputfarm;
    }

    public inputFarm(int[] inputfarm){
        this.inputfarm = inputfarm;
    }
}
