package com.example.proapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

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
        header = (ImageView) findViewById(R.id.header);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        getincomingIntent();



    }

    private void getincomingIntent() {

        if (getIntent() != null) {
            String imageurl = getIntent().getStringExtra("item_image");
            String title = getIntent().getStringExtra("item_title");
            collapsingToolbar.setTitle(title);
            Glide.with(ProdetalActivity.this).load(imageurl).into(header);

        }
    }
}
