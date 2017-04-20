package edu.fontys.sm41.giffel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.UUID;

import co.lujun.androidtagview.ColorFactory;
import co.lujun.androidtagview.TagContainerLayout;

public class DetailActivity extends AppCompatActivity implements FloatingActionButton.OnClickListener {

    private ImageView imageView;
    private ImageView avatarImage;
    private TextView userNameText;
    private ProgressBar spinner;
    private FloatingActionButton floatingActionButton;
    private FloatingActionButton closeButton;
    private TagContainerLayout tagView;

    private Gif gif;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_detail);

        spinner = (ProgressBar) findViewById(R.id.spinner);
        imageView = (ImageView) findViewById(R.id.imageView);
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        userNameText = (TextView) findViewById(R.id.userNameText);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        closeButton = (FloatingActionButton) findViewById(R.id.closeButton);
        tagView = (TagContainerLayout) findViewById(R.id.tagView);

        Bundle data = getIntent().getExtras();

        if (data ==  null){ return; }
        gif = (Gif) data.get("gif");

        spinner.setVisibility(View.VISIBLE);

        loadGif();

        Glide.with(this).load(gif.getAvatar()).into(avatarImage);
        userNameText.setText(gif.getDisplayName());
        floatingActionButton.setOnClickListener(this);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setTagViewSettings(){
        tagView.setBackgroundColor(Color.TRANSPARENT);
        tagView.setBorderWidth(0);
        tagView.setBorderColor(Color.TRANSPARENT);
        tagView.setBorderRadius(0);
        tagView.setTagBorderRadius(8);
        tagView.setTagTextSize(48);
        tagView.setTagBorderWidth(0);
        tagView.setTagBorderColor(Color.TRANSPARENT);
        tagView.setHorizontalInterval(8);
        tagView.setVerticalInterval(8);
    }

    private void loadGif(){

        if (gif.getTags() != null){
            setTagViewSettings();
            tagView.setTags(this.gif.getTags());
        }

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

        final Context context = floatingActionButton.getContext();


        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference ref =  storage.getReferenceFromUrl(this.gif.getImageUrl());

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                verifyStoragePermissions(DetailActivity.this);

                File d = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File f = new File(d, UUID.randomUUID().toString() + ".gif");
                Ion.with(context)
                        .load(uri.toString())
                        .write(f)
                        .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File result) {
                        Log.d("ION", "onCompleted: " + result);

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("image/gif");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(result));
                        context.startActivity(Intent.createChooser(intent, "Share gif"));
                    }
                });





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Storage", "onFailure: Couldn't get download URL" );
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
