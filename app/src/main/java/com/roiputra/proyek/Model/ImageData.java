package com.roiputra.proyek.Model;


import com.google.gson.annotations.SerializedName;

import retrofit2.http.GET;

public class ImageData {
    private String image;
    private String id;
    @SerializedName("mini_image")
    private String mini_image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMini_image() {
        return mini_image;
    }

    public void setMini_image(String mini_image) {
        this.mini_image = mini_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
