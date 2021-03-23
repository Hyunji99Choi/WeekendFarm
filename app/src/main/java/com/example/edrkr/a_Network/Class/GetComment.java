package com.example.edrkr.a_Network.Class;

import com.google.gson.annotations.SerializedName;

public class GetComment implements Post{ //댓글을 가져오는 클래스

    @SerializedName("id")
    private final int id;

    @SerializedName("nickname")
    private final String nickname;

    @SerializedName("Content")
    private final String content;

    @SerializedName("CommentCnt")
    private final int commentCnt;

    @SerializedName("date")
    private final String date;

    public GetComment(int id, String username, String content, int postnum, String time){
        this.id = id;
        this.nickname = username;
        this.content = content;
        this.commentCnt = postnum;
        this.date = time;
    }

    public int getId() {
        return id;
    }
    public String getUsername() {
        return nickname;
    }
    public String getContent() {
        return content;
    }
    public int getPostnum() {
        return commentCnt;
    }
    public String getTime() {
        return date;
    }
}
