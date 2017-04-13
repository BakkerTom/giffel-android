package edu.fontys.sm41.giffel;

import java.io.Serializable;

/**
 * Created by tom on 30/03/2017.
 */
public class Gif implements Serializable{

    private String imageUrl;

    public Gif(){}

    public Gif(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
