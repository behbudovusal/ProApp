package com.example.proapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    EditText datetext, timetext;
    TextInputEditText customertext, pricetext, titletext, adresstext, desctext, phonetext;
    DatePickerDialog picker;
    ImageView chooseimg;
    private int PICK_IMAGE_REQUEST = 1;
    int day, month, year, hour, minut;
    Button addbtn;
    Products product;
    Long tsLong;
    String ts;
    Uri uri;
    DatabaseReference ref;
    ProgressBar mprogressbar;

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
        mprogressbar=findViewById(R.id.progressbar);
        addbtn.setOnClickListener(this);
        datetext.setOnClickListener(this);
        timetext.setOnClickListener(this);
        product = new Products();
        ref = FirebaseDatabase.getInstance().getReference().child("product");
       //get current time and date
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
    //get choosen img uri
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            Glide.with(this).load(uri).into(chooseimg);
        }
    }
//choose imgage
    public void openFilechoose(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

//clear edittext
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

            //upload data
            case R.id.add: {
                tsLong = System.currentTimeMillis() / 1000;
                ts = tsLong.toString();
                uploadImagetoFirebase(ts,uri);
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

    private void uploadImagetoFirebase(String ts, Uri uri) {

        // Create a storage reference from our app
        if (uri != null)
        {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploadimage");
            StorageReference imageRef = storageRef.child(ts + "/img." + getFileExtension(uri));
            imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getApplicationContext(),taskSnapshot.getUploadSessionUri().toString(),Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"Şəkil seçin",Toast.LENGTH_LONG).show();
        }

    }
    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}

