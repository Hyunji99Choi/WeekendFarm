package com.example.edrkr.bulletinPage;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    private int pos; //게시글 번호
    private String title; //게시글 제목
    private String name = "name"; //작성자 이름
    private String date = "0년 0월 0일  0:0"; //작성 시간
    private int chat_count = 0; //댓글 개수
    private int good_count = 0; //좋아요 숫자 - 없어질 예정
    private String body; //게시글 본문
    private ArrayList<Comment> comments = new ArrayList<>(); //댓글 저장하는 arraylist

    Board() {} // 생성자
    Board(int pos){
        this.pos = pos;
    } //생성자2
    public Board(int pos, String name, String title, String body, int commentnum, String time){ //생성자3 - 다른 클래스에서도 사용
        Log.v("Board","Board진입완료");
        this.pos = pos;
        this.name = name;
        this.title = title;
        this.body = body;
        this.chat_count = commentnum;
        this.date = time;
    }

    //setter & getter - alt + ins 단축기로 간단 생성 가능
    public void setPos(int pos) {this.pos = pos;}
    public String getTitle(){return title;}
    public String getBody(){return body;}
    public String getDate() { return date; }
    public int getChat_count() { return chat_count;}
    public int getGood_count(){return good_count;}
    public String getName() { return name;}
    public ArrayList<Comment> getComments() {
        return comments;
    }

    public int getPos() {return pos;}
    public void setTitle(String title){this.title = title;}
    public void setBody(String contents){this.body = contents;}
    public void setChat_count(int chat_count) {this.chat_count = chat_count;}
    public void plusChat_count(){this.chat_count++;}
    public void setDate(String date) { this.date = date;  }
    public void setGood_count(int good_count){this.good_count = good_count;}
    public void setName(String name) { this.name = name;}
   // public void setComments(ArrayList<Comment> comments) {  this.comments = comments;   }
    public void addComment(Comment c){this.comments.add(c);}
}
