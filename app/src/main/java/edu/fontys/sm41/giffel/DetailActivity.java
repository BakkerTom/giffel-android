package edu.fontys.sm41.giffel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity implements FloatingActionButton.OnClickListener {

    private ImageView imageView;
    private ImageView avatarImage;
    private TextView userNameText;
    private ProgressBar spinner;
    private FloatingActionButton floatingActionButton;

    private Gif gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        spinner = (ProgressBar) findViewById(R.id.spinner);
        imageView = (ImageView) findViewById(R.id.imageView);
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        userNameText = (TextView) findViewById(R.id.userNameText);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        Bundle data = getIntent().getExtras();

        if (data ==  null){ return; }
        gif = (Gif) data.get("gif");

        spinner.setVisibility(View.VISIBLE);

        loadGif();

        Glide.with(this).load(gif.getAvatar()).into(avatarImage);
        userNameText.setText(gif.getDisplayName());
        floatingActionButton.setOnClickListener(this);
    }

    private void loadGif(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref =  storage.getReferenceFromUrl(this.gif.getImageUrl());

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(DetailActivity.this)
                        .load(uri.toString()).asGif()
                        .listener(new RequestListener<String, GifDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                spinner.setVisibility(View.GONE);
                                return false;
                            }
                        })
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
        Context context = floatingActionButton.getContext();
        Intent intent = new Intent(context, Intent.ACTION_SEND_MULTIPLE.getClass());
        context.startActivity(intent);
    }
}
