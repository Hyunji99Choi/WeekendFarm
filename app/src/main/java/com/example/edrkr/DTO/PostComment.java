package com.example.edrkr.DTO;

import com.google.gson.annotations.SerializedName;

public class PostComment implements Post{

    @SerializedName("id")
    private final int id;

    @SerializedName("UserName")
    private final String username;

    @SerializedName("Content")
    private final String content;

    @SerializedName("PostNum")
    private final int postnum;

    @SerializedName("createdAt")
    private final String time;

    public PostComment(int id, String username,String content, int postnum,String time){
        this.id = id;
        this.username = username;
        this.content = content;
        this.postnum = postnum;
        this.time = time;
    }

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getContent() {
        return content;
    }
    public int getPostnum() {
        return postnum;
    }
    public String getTime() {
        return time;
    }
}
