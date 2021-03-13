package com.example.edrkr.a_Network;

import com.google.gson.annotations.SerializedName;

public class GetComment implements Post{ //댓글을 가져오는 클래스

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

    public GetComment(int id, String username, String content, int postnum, String time){
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
