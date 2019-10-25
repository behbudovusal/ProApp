package com.example.proapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog dialog;
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
    LinearLayout liner;
    TextInputLayout customerlayout, pricelayout, titlelayout, phonelayout;

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
        liner = findViewById(R.id.textinput_linear);
        customerlayout = findViewById(R.id.customerlayout);
        pricelayout = findViewById(R.id.pricelayout);
        titlelayout = findViewById(R.id.titlelayout);
        phonelayout = findViewById(R.id.phonelayout);
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
//        datetext.setText(day + "." + (month + 1) + "." + year);
//        datetext.setInputType(InputType.TYPE_NULL);
//        timetext.setInputType(InputType.TYPE_NULL);
//        timetext.setText(hour + " : " + minut);
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

    //choose image
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
                if (validateAll()) {
                    tsLong = System.currentTimeMillis() / 1000;
                    ts = tsLong.toString();

                    if (uri != null) {
                        {
                            progress();
                            uploaddata();
                            uploadImagetoFirebase(ts, uri);

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Şəkil seçin", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            }
            case R.id.date: {
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
            case R.id.time: {
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

    private void uploadImagetoFirebase(final String ts, Uri uri) {

        if (uri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploadimage");
            final StorageReference imageRef = storageRef.child(ts + "/img." + getFileExtension(uri));
            Task<Uri> task = imageRef.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    String downloadURL = downloadUri.toString();
                    ref.child(ts).child("img").setValue(downloadURL);
                }
            });
        }
    }

    private void uploaddata() {
        product.setTitle(titletext.getText().toString());
        product.setCustomer(customertext.getText().toString());
        product.setAdress(adresstext.getText().toString());
        product.setDate(datetext.getText().toString());
        product.setTime(timetext.getText().toString());
        product.setDescription(desctext.getText().toString());
        product.setPhone(phonetext.getText().toString());
        product.setPrice(pricetext.getText().toString());
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
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void progress() {
        dialog = new ProgressDialog(AddActivity.this);
        dialog.setMessage("Yüklənir..."); // Setting Message
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        dialog.show(); // Display Progress Dialog
        dialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                Intent intent=new Intent(AddActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).start();
    }

    private boolean validateText(TextInputEditText txtinput, TextInputLayout txtlayout) {
        String input = txtinput.getText().toString();
        if (input.isEmpty()) {
            txtlayout.setError(input + " daxil edin");
            return false;
        } else {
            txtlayout.setError(null);
            return true;
        }
    }

    private boolean validatePhone(TextInputEditText txtinput, TextInputLayout txtlayout) {
        String input = txtinput.getText().toString();
        if (input.isEmpty()) {
            txtlayout.setError(input + " daxil edin");
            return false;

        } else if (input.length() < 10) {

            txtlayout.setError(input + " telefon nömrəsini düzgün daxil edin");
            return false;

        } else {
            txtlayout.setError(null);
            return true;
        }
    }
    private boolean validateAll() {
        boolean validateCustomer = validateText(customertext, customerlayout);
        boolean validateTitle = validateText(titletext, titlelayout);
        boolean validatePrice = validateText(pricetext, pricelayout);
        boolean phonevalidate = validatePhone(phonetext, phonelayout);
        return validateCustomer && validatePrice && validateTitle && phonevalidate;
    }
}

