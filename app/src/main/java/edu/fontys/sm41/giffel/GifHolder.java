package edu.fontys.sm41.giffel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by tom on 30/03/2017.
 */
public class GifHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private Gif gif;

    private ImageView imageView;

    public GifHolder(View view) {
        super(view);

        imageView = (ImageView) view.findViewById(R.id.imageView);

        view.setOnClickListener(this);
    }

    public void bindGif(Gif gif) {
        this.gif = gif;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref =  storage.getReferenceFromUrl(gif.getImageUrl());

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(itemView.getContext())
                        .load(uri.toString())
                        .asGif()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Storage", "onFailure: Couldn't get download URL" );
            }
        });

    }

    @Override
    public void onClick(View v) {
        Context context = itemView.getContext();
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("gif", this.gif);
        context.startActivity(intent);
    }
}
