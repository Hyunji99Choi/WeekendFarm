package com.example.edrkr;

import java.io.Serializable;

public class Comment implements Serializable {
    private String name = "name";
    private String date = "0년 0월 0일  0:0";
    private String body;
    private int chat_count = 0;
    private int good_count = 0;

    Comment(){}

    Comment(String name, String body, String date ){
        this.name = name;
        this.date = date;
        this.body = body;
    }

    public String getBody(){return body;}
    public String getDate() { return date; }
    public int getChat_count() { return chat_count;}
    public int getGood_count(){return good_count;}
    public String getName() { return name;}

    public void setBody(String contents){this.body = contents;}
    public void setChat_count(int chat_count) {this.chat_count = chat_count;}
    public void setDate(String date) { this.date = date;  }
    public void setGood_count(int good_count){this.good_count = good_count;}
    public void setName(String name) { this.name = name;}
}
