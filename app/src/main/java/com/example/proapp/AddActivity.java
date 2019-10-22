package com.example.proapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    EditText datetext, timetext;
    TextInputEditText customertext, pricetext, titletext, adresstext, desctext, phonetext;
    DatePickerDialog picker;
    ImageView chooseimg;
    private int PICK_IMAGE_REQUEST = 1;
    int day, month, year, hour, minut;
    Button addbtn;
    DatabaseReference ref;
    Products product;
    Long tsLong;
    String ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        datetext = findViewById(R.id.date);
        chooseimg = findViewById(R.id.img);
        timetext = findViewById(R.id.time);
        addbtn = findViewById(R.id.add);
        customertext = findViewById(R.id.customer);
        pricetext = findViewById(R.id.price);
        titletext = findViewById(R.id.title);
        adresstext = findViewById(R.id.adress);
        desctext = findViewById(R.id.description);
        phonetext = findViewById(R.id.phone);

        product = new Products();
        ref = FirebaseDatabase.getInstance().getReference().child("product");
        addbtn.setOnClickListener(this);
        datetext.setOnClickListener(this);
        timetext.setOnClickListener(this);
        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        hour = cldr.get(Calendar.HOUR_OF_DAY);
        minut = cldr.get(Calendar.MINUTE);
        datetext.setText(day + "." + (month + 1) + "." + year);
        datetext.setInputType(InputType.TYPE_NULL);
        timetext.setInputType(InputType.TYPE_NULL);
        timetext.setText(hour + " : " + minut);
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

    public void cleartext() {
        customertext.getText().clear();
        desctext.getText().clear();
        titletext.getText().clear();
        pricetext.getText().clear();
        phonetext.getText().clear();
        adresstext.getText().clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add: {
                tsLong = System.currentTimeMillis() / 1000;
                ts = tsLong.toString();
                product.setTitle(titletext.getText().toString());
                product.setCustomer(customertext.getText().toString());
                product.setAdress(adresstext.getText().toString());
                product.setDate(datetext.getText().toString());
                product.setTime(timetext.getText().toString());
                product.setDescription(desctext.getText().toString());
                product.setPhone(phonetext.getText().toString());
                product.setId(ts);
                ref.child(ts).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        cleartext();
                        Toast.makeText(getApplicationContext(), "Məlumatlar yükləndi", Toast.LENGTH_LONG).show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                    }
                });
                break;
            }
            case R.id.date:
            {
                // date picker dialog
                picker = new DatePickerDialog(AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                datetext.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                            }
                        }, year, month, day);

                picker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Ləğv et", picker);
                picker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", picker);
                picker.show();
                break;
            }
            case R.id.time:
            {
                //clock dialog
                TimePickerDialog timer = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timetext.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minut, true);
                timer.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Ləğv et", timer);
                timer.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", timer);
                timer.show();
                break;
            }
        }
    }
}

