package com.example.edrkr.a_Network.Class.manager;

import com.google.gson.annotations.SerializedName;

public class InputFarm {
    @SerializedName("inputFarm")
    int inputfarm;

    public int getInputfarm() {
        return inputfarm;
    }

    public void setInputfarm(int inputfarm) {
        this.inputfarm = inputfarm;
    }

    public InputFarm(int inputfarm){
        this.inputfarm = inputfarm;
    }
}
