package com.example.proapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Objects;

public class ProdetalActivity extends AppCompatActivity {


    ImageView header;
    TextView txtcustomer,txtadress=null,txtprice,txtphone,txtdate,txtdesc;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prodetail);
        toolbar = findViewById(R.id.anim_toolbar);
        header =  findViewById(R.id.header);
        txtcustomer=findViewById(R.id.txtcustomer);
        txtadress=findViewById(R.id.txtadress);
        txtprice=findViewById(R.id.txtprice);
        txtphone=findViewById(R.id.txtphone);
        txtdate=findViewById(R.id.txtdate);
        txtdesc=findViewById(R.id.txtdescription);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getincomingIntent();



    }

    private void getincomingIntent() {

        if (getIntent() != null) {

            String title = getIntent().getStringExtra("item_title");
            String customer = getIntent().getStringExtra("item_customer");
            String price = getIntent().getStringExtra("item_price");
            String adress = getIntent().getStringExtra("item_adress");
            String date = getIntent().getStringExtra("item_date");
            String time = getIntent().getStringExtra("item_time");
            String desc = getIntent().getStringExtra("item_desc");
            String id = getIntent().getStringExtra("item_id");
             phone = getIntent().getStringExtra("item_phone");
            String imageurl = getIntent().getStringExtra("item_image");
            collapsingToolbar.setTitle(title);
            setheaderimgage(imageurl);
            setdata(customer,price,adress,date,time,desc,phone);


        }

    }
    private void setheaderimgage(String imageurl)
    {
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
    private void setdata(String customer, String price, String adress, String date, String time, String desc, String phone)
    {
        txtcustomer.setText(customer);
        txtadress.setText(adress);
        txtprice.setText(price+" manat ");
        txtphone.setText(phone);
        if (date.isEmpty()||time.isEmpty()) {
            txtdate.setText(date + time);
        }
        else {
            txtdate.setText(date+" saat "+time);
        }
        txtdesc.setText(desc);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void callPhone(View view) {
        Intent intent=new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        startActivity(intent);

    }
}
