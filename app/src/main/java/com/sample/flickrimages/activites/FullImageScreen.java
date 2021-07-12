package com.sample.flickrimages.activites;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.sample.flickrimages.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class FullImageScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CharSequence title =getIntent().getStringExtra(GalleryImageScreen.FULLSCREEN_TITLE);
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        setContentView(R.layout.activity_full_image_screen);
        ImageView imageView = findViewById(R.id.gallery_full_image_view);
        ProgressBar mBar = findViewById(R.id.image_loader);
        String imageUrl = getIntent().getStringExtra(GalleryImageScreen.FULLSCREEN_IMAGE_URL);
        Picasso.get().load(imageUrl).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("CostcoImage","CostcoImage Full Screen Image success");
                mBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                Log.d("CostcoImage","CostcoImage Full Screen Image Failure");
                mBar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar
                        .make(imageView, "Image Download Failed with error ->"+e.getMessage(), Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", view -> FullImageScreen.this.finish());
                snackbar.show();
            }
        });


    }
}