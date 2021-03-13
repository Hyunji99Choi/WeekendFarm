package com.example.edrkr.a_Network.Class;

import com.google.gson.annotations.SerializedName;

public class PostComment { //작성한 댓글을 보내는 클래스
    @SerializedName("name")
    private String name;

    @SerializedName("content")
    private String content;

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
}

