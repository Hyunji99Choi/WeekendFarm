package com.example.edrkr.a_Network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetEachBoard { //하나의 게시글을 선택해서 가져오는 클래스
    @SerializedName("Post")
    private final List<GetBoard> post;

    @SerializedName("Comment")
    private final List<GetComment> comment;

    public GetEachBoard(List<GetBoard> post, List<GetComment> comment) {
        this.post = post;
        this.comment = comment;
    }

    public List<GetBoard> getPost() {
        return post;
    }

    public List<GetComment> getComment() {
        return comment;
    }
}
