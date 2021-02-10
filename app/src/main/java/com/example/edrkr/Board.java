package com.example.edrkr;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    private int pos;
    private String title;
    private String name = "name";
    private String date = "0년 0월 0일  0:0";
    private int chat_count = 0;
    private int good_count = 0;
    private String body;
    private ArrayList<Comment> comments = new ArrayList<>();

    Board() {}
    Board(int pos){
        this.pos = pos;
    }
    Board(int pos, String name, String title, String body, int commentnum, String time){
        Log.v("Board","Board진입완료");
        this.pos = pos;
        this.name = name;
        this.title = title;
        this.body = body;
        this.chat_count = commentnum;
        this.date = time;
    }

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
