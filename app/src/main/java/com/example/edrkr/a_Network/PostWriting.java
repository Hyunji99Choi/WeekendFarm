package com.example.edrkr.a_Network;

import com.google.gson.annotations.SerializedName;

public class PostWriting implements Post{
    @SerializedName("name")
    private String name;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;


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
                "name="+ name+
                ", title='" +title+'\''+
                ", content='"+content+'\''+
                '}';
    }
}
