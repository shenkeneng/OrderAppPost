package com.frxs.order.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ewu on 2016/5/6.
 */
public class PictureDetails implements Serializable {

    @SerializedName("ImageUrlOrg")
    private String ImageUrlOrg;

    public String getImageUrlOrg() {
        return ImageUrlOrg;
    }

    public void setImageUrlOrg(String imageUrlOrg) {
        ImageUrlOrg = imageUrlOrg;
    }
}
