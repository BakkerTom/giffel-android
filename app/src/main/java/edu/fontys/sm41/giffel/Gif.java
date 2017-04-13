package edu.fontys.sm41.giffel;

import java.io.Serializable;

/**
 * Created by tom on 30/03/2017.
 */
public class Gif implements Serializable{

    private String imageUrl;
    private String displayName;
    private String avatar;
    private String userId;

    public Gif(){}

    public Gif(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUserId() {
        return userId;
    }
}
