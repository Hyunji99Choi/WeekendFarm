package com.example.edrkr.a_Network.Class;

import com.google.gson.annotations.SerializedName;

public class PostComment { //작성한 댓글을 보내는 클래스
    @SerializedName("nickname")
    private String nickname;

    @SerializedName("content")
    private String content;

    @SerializedName("userIdent ")
    private int userIdent;

    public String getName() {
        return nickname ;
    }
    public void setName(String name) {
        this.nickname  = name;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getUserIdent() {
        return userIdent;
    }
    public void setUserIdent(int userIdent) {
        this.userIdent = userIdent;
    }
}

