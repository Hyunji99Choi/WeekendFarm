package com.example.edrkr.a_Network.Class.bulletin;

import com.example.edrkr.a_Network.Class.Post;
import com.google.gson.annotations.SerializedName;

public class PatchBoard implements Post {
    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

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


}
