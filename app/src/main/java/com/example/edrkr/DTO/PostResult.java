package com.example.edrkr.DTO;

import com.google.gson.annotations.SerializedName;

//DTO 모델 - PostResult Class 선언
public class PostResult implements Post{ //Board 클래스의 PostResult Class

    //@SerializedName으로 일치시켜 주지 않을 경우에는 클래스 변수명이 일치해야 함.
    //@SerializedName으로 변수명을 일치시켜주면 클래스 변수 명이 달라도 알아서 매핑시켜줌.
    @SerializedName("id")
    private int id;

    @SerializedName("UserName")
    private String name;

    private String title;

    @SerializedName("Content")
    private String body;

    @SerializedName("CommentNum")
    private int commentNum;

    @SerializedName("createdAt")
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //toString()을 Override 해주지 않으면 객체 주소값을 출력함.
    @Override
    public String toString(){
        return "PostResult{"+
                "Id="+ id+
                ", UserName="+ name+
                ", title='" +title+'\''+
                ", content='"+body+'\''+
                ", CommentNum='"+commentNum+'\''+
                ", createdAt='"+time+'\''+
                '}';
    }
}