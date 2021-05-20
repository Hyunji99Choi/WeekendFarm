package com.example.edrkr.a_Network.Class.bulletin;

import com.google.gson.annotations.SerializedName;

public class GetImage {
    @SerializedName("image")
    private String imageUrl;

    public GetImage(String url){ this.imageUrl = url;}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
