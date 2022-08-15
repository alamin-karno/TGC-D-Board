package com.teamblank.tgcdboard.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.teamblank.tgcdboard.R;
import com.teamblank.tgcdboard.model.Teacher;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddTeacherActivity extends AppCompatActivity {

    private TextView titleTV,addImageTV;
    private EditText nameET,designationET,emailET,phoneET;
    private ImageView imageView,cancelImage;
    private Button addBTN;

    private Uri imageUri;
    private String checker = "";
    private DatabaseReference teacherRef;
    private ProgressDialog loadingBar;
    private String name,designation,email,phone,
            saveCurrtenDate,saveCurrentTime,productRandomKey;
    private String downloadImageURL = "";
    private StorageReference teacherImageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        init();

        onAddImageClick();

        onCancelImageClicked();

        onAddBTNClicked();
    }

    private void onAddBTNClicked() {
        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString();
                designation = designationET.getText().toString();
                phone = phoneET.getText().toString();
                email = emailET.getText().toString();

                if(name.isEmpty()){
                    nameET.setError("Please enter name");
                }
                else if(designation.isEmpty()){
                    designationET.setError("Please enter designation");
                }
                else if(phone.isEmpty()){
                    phoneET.setError("Please enter phone number");
                }
                else if(email.isEmpty()){
                    emailET.setError("Please enter email address");
                }
                else {

                    productRandomKey = name+" "+designation;

                    Loading();

                    if(checker.equals("clicked"))
                    {
                        uploadImage();
                    }
                    else
                    {
                        AddInfoToDatabase();
                    }
                }
            }
        });
    }

    private void uploadImage() {

        final StorageReference filePath = teacherImageRef.child(productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddTeacherActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddTeacherActivity.this, "Image Uploaded Successfully.", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){

                            downloadImageURL = task.getResult().toString();

                            Toast.makeText(AddTeacherActivity.this, "Image url got successfully.", Toast.LENGTH_SHORT).show();

                            AddInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void AddInfoToDatabase() {


        HashMap<String, Object> teacherMap = new HashMap<>();
        teacherMap.put("tID",productRandomKey);
        teacherMap.put("tName",name);
        teacherMap.put("tDesignation",designation);
        teacherMap.put("tEmail",email);
        teacherMap.put("tPhone",phone);
        teacherMap.put("tSearch",name.toLowerCase());
        teacherMap.put("tImage",downloadImageURL);

        teacherRef.child(productRandomKey).updateChildren(teacherMap).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AddTeacherActivity.this, "Added Successfully.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            startActivity(new Intent(AddTeacherActivity.this,TeacherHomeActivity.class));
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddTeacherActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });
    }

    private void Loading() {
        loadingBar.setTitle("Uploading Information...");
        loadingBar.setMessage("Please wait until the information is successfully upload.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    private void onCancelImageClicked() {

        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageUri = null;
                checker = "";
                imageView.setVisibility(View.INVISIBLE);
                cancelImage.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void onAddImageClick() {
        addImageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checker = "clicked";

                CropImage.activity(imageUri)
                        .start(AddTeacherActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            imageView.setVisibility(View.VISIBLE);
            cancelImage.setVisibility(View.VISIBLE);
            imageView.setImageURI(imageUri);
        }
        else {
            checker = "";
            Toast.makeText(this, "Error, Try Again...", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {

        titleTV = findViewById(R.id.titleTV);
        addImageTV = findViewById(R.id.addImage);
        nameET = findViewById(R.id.addNameET);
        designationET = findViewById(R.id.addDesignationET);
        emailET = findViewById(R.id.addEmailET);
        phoneET = findViewById(R.id.addPhoneET);
        imageView = findViewById(R.id.imageView);
        cancelImage = findViewById(R.id.cancelImage);
        addBTN = findViewById(R.id.addBTN);

        loadingBar = new ProgressDialog(this);

        teacherImageRef = FirebaseStorage.getInstance().getReference().child("Teacher Pictures");
        teacherRef = FirebaseDatabase.getInstance().getReference().child("Teacher List");
    }

    public void back(View view) {
        finish();
    }
}