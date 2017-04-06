package edu.fontys.sm41.giffel;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by tom on 30/03/2017.
 */
public class GifHolder extends RecyclerView.ViewHolder{

    private Gif gif;

    private ImageView gifImageView;

    public GifHolder(View view) {
        super(view);

        gifImageView = (ImageView) view.findViewById(R.id.imageView);
    }

    public void bindGif(Gif gif) {
        this.gif = gif;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReferenceFromUrl(gif.getImageUrl());

        Glide.with(itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(ref)
                .into(gifImageView);
    }
}
