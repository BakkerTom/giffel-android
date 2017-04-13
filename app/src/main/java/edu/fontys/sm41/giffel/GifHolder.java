package edu.fontys.sm41.giffel;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by tom on 30/03/2017.
 */
public class GifHolder extends RecyclerView.ViewHolder{

    private Gif gif;

    private ImageView imageView;

    public GifHolder(View view) {
        super(view);

        imageView = (ImageView) view.findViewById(R.id.imageView);
    }

    public void bindGif(Gif gif) {
        this.gif = gif;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref =  storage.getReferenceFromUrl(gif.getImageUrl());

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(itemView.getContext()).load(uri.toString()).asGif().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Storage", "onFailure: Couldn't get download URL" );
            }
        });

    }
}
