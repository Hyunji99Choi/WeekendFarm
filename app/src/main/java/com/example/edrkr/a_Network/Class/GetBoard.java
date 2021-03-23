package com.example.edrkr.a_Network.Class;

import com.google.gson.annotations.SerializedName;

//DTO 모델 - PostResult Class 선언
public class GetBoard implements Post { //Board를 가져오는 클래스

    //@SerializedName으로 일치시켜 주지 않을 경우에는 클래스 변수명이 일치해야 함.
    //@SerializedName으로 변수명을 일치시켜주면 클래스 변수 명이 달라도 알아서 매핑시켜줌.
    @SerializedName("id")
    private final int id;

    @SerializedName("UserNickName")
    private final String UserNickName;

    @SerializedName("Title")
    private final String title;

    @SerializedName("Content")
    private final String body;

    @SerializedName("commentCnt")
    private final int commentCnt;

    @SerializedName("date")
    private final String date;

    public GetBoard(int id, String title, String name, String body, int commentNum, String time) {
        this.id = id;
        this.UserNickName = name;
        this.title = title;
        this.body = body;
        this.commentCnt = commentNum;
        this.date = time;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return UserNickName;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getCommentNum() {
        return commentCnt;
    }

    public String getTime() {
        return date;
    }


    //toString()을 Override 해주지 않으면 객체 주소값을 출력함.
    @Override
    public String toString(){
        return "PostResult{"+
                "Id="+ id+
                ", UserName="+ UserNickName+
                ", title='" +title+'\''+
                ", content='"+body+'\''+
                ", CommentNum='"+commentCnt+'\''+
                ", createdAt='"+date+'\''+
                '}';
    }
}