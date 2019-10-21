package com.example.proapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ProdetalActivity extends AppCompatActivity {


    ImageView header;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prodetail);
        toolbar = findViewById(R.id.anim_toolbar);
        header =  findViewById(R.id.header);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getincomingIntent();
        String imageurl = getIntent().getStringExtra("item_image");

        //bitmap
        Glide.with(ProdetalActivity.this).load(imageurl).into(header);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.camera);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                collapsingToolbar.setContentScrimColor(mutedColor);
                getWindow().setStatusBarColor(mutedColor);
            }
        });
    }

    private void getincomingIntent() {

        if (getIntent() != null) {

            String title = getIntent().getStringExtra("item_title");
            collapsingToolbar.setTitle(title);


        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
