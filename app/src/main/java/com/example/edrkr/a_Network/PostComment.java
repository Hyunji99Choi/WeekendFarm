package com.example.edrkr.a_Network;

import com.google.gson.annotations.SerializedName;

public class PostComment {
    @SerializedName("name")
    private String name;

    @SerializedName("content")
    private String content;
//
//    @SerializedName("id")
//    private String id;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
//    public String getId() {
//        return id;
//    }
//    public void setId(String id) {
//        this.id = id;
//    }
}

