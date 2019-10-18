package com.example.proapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    EditText datetext;
    DatePickerDialog picker;
    ImageView chooseimg;
    private int PICK_IMAGE_REQUEST = 1;
    EditText timetext;
    int day, month, year;
    int hour, minut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        datetext = findViewById(R.id.date);
        chooseimg = findViewById(R.id.img);
        timetext = findViewById(R.id.time);

        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        hour = cldr.get(Calendar.HOUR_OF_DAY);
        minut = cldr.get(Calendar.MINUTE);
        datetext.setText(day + "." + (month + 1) + "." + year);
        datetext.setInputType(InputType.TYPE_NULL);
        timetext.setInputType(InputType.TYPE_NULL);
        timetext.setText(hour+" : "+minut);
        datetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // date picker dialog
                picker = new DatePickerDialog(AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                datetext.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                            }
                        }, year, month, day);

                picker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Ləgv et", picker);
                picker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", picker);
                picker.show();


            }
        });

        timetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timer=new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timetext.setText(hourOfDay+":"+minute);
                    }
                },hour,minut,true);
                timer.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Ləgv et", timer);
                timer.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", timer);
                timer.show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Glide.with(this).load(uri).into(chooseimg);

        }
    }

    public void openFilechoose(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }
}

