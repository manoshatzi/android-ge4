package com.example.manoshatzi.ge4_sdy61;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


public class ShowImages extends AppCompatActivity {

    private static String imageUrl = "https://unsplash.it/200/300/?random";
    private ImageView imageView;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);
        mp = MediaPlayer.create(this, R.raw.button_sound);

        imageView = (ImageView)findViewById(R.id.imageView);
        loadImageFromUrl(imageUrl);
    }

    private void loadImageFromUrl(String url) {
        Log.d("Image", "load Image called");
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.progress_animation)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.mipmap.ic_launcher)
                .into(imageView, new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() { }

                    @Override
                    public void onError() { }
                });
    }

    public void getAnotherImage(View view) {
        mp.start();
        loadImageFromUrl(imageUrl);
    }

    public void goToGeofencing(View view) {
        mp.start();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
