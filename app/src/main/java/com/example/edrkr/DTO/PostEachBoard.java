package com.example.edrkr.DTO;

import com.example.edrkr.Bulletin.Comment;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostEachBoard {
    @SerializedName("Post")
    private final List<PostResult> post;

    @SerializedName("Comment")
    private final List<PostComment> comment;

    public PostEachBoard(List<PostResult> post, List<PostComment> comment) {
        this.post = post;
        this.comment = comment;
    }

    public List<PostResult> getPost() {
        return post;
    }

    public List<PostComment> getComment() {
        return comment;
    }
}
