package edu.fontys.sm41.giffel;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

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
        StorageReference ref = storage.getReferenceFromUrl(this.gif.getImageUrl());

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(itemView.getContext())
                        .load(uri.toString())
                        .into(gifImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Storage", "onFailure: Couldn't get download URL" );
            }
        });

    }
}
