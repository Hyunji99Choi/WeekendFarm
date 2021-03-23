package com.example.edrkr.a_Network.Class;

import com.google.gson.annotations.SerializedName;

public class PostBoard implements Post{ //작성한 글을 보내는 클래스
    @SerializedName("nickname ")
    private String nickname ;

    @SerializedName("userIdent")
    private int userIdent;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;


    public int getUserIdent() {
        return userIdent;
    }

    public void setUserIdent(int userIdent) {
        this.userIdent = userIdent;
    }

    public String getNickname() {
        return nickname ;
    }

    public void setNickname(String name) {
        this.nickname  = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //toString()을 Override 해주지 않으면 객체 주소값을 출력함.
    @Override
    public String toString(){
        return "PostResult{"+
                "name="+ nickname +
                "\n, userident="+userIdent+
                ", title='" +title+'\''+
                ", content='"+content+'\''+
                '}';
    }
}
