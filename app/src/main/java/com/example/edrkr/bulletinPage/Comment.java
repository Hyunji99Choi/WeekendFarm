package com.example.edrkr.bulletinPage;

import java.io.Serializable;

public class Comment implements Serializable {
    private int id  = -1; //서버에서의 번호
    private String name = "name"; //작성자 이름
    private String date = "0년 0월 0일  0:0"; //작성일
    private String body; //댓글 본문
    private int chat_count = 0; //댓글 개수 - 삭제 예정
    private int good_count = 0; //좋아요 개수 - 삭제 예정

    Comment(){} //생성자

    Comment(int id,String name, String body, String date ){ //생성자 - 타 클래스에서 사용
        this.id = id;
        this.name = name;
        this.date = date;
        this.body = body;
    }

    //getter & setter
    public String getBody(){return body;}
    public String getDate() { return date; }
    public int getChat_count() { return chat_count;}
    public int getGood_count(){return good_count;}
    public String getName() { return name;}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setBody(String contents){this.body = contents;}
    public void setChat_count(int chat_count) {this.chat_count = chat_count;}
    public void setDate(String date) { this.date = date;  }
    public void setGood_count(int good_count){this.good_count = good_count;}
    public void setName(String name) { this.name = name;}
}
