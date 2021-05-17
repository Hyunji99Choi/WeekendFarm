package com.example.edrkr.a_Network.Class.manager;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class inputUser {
    @SerializedName("inputUser")
    int[] inputUser;

    public int[] getInputUser() {
        return inputUser;
    }

    public void setInputUser(int[] inputfarm) {
        this.inputUser = inputfarm;
    }

    public inputUser(int[] inputfarm){
        this.inputUser = inputfarm;
    }
}
