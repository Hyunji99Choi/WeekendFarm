package com.example.edrkr.a_Network.Class;

import com.google.gson.annotations.SerializedName;

public class PatchComment implements Post{

    @SerializedName("content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
