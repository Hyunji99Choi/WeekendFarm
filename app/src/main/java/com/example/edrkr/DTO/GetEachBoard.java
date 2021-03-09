package com.example.edrkr.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetEachBoard {
    @SerializedName("Post")
    private final List<GetResult> post;

    @SerializedName("Comment")
    private final List<GetComment> comment;

    public GetEachBoard(List<GetResult> post, List<GetComment> comment) {
        this.post = post;
        this.comment = comment;
    }

    public List<GetResult> getPost() {
        return post;
    }

    public List<GetComment> getComment() {
        return comment;
    }
}
