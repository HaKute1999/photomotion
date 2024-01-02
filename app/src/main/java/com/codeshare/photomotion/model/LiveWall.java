package com.codeshare.photomotion.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LiveWall implements Serializable {
    @SerializedName("video_url")
    String video_url;
    @SerializedName("image_url")
    String image_url;
    @SerializedName("title")
    String title;

    public LiveWall(String video_url, String image_url) {
        this.video_url = video_url;
        this.image_url = image_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
