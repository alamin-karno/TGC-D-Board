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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.teamblank.tgcdboard.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddNoticeActivity extends AppCompatActivity {

    private EditText noticeTitleET,noticeDetailsET;
    private TextView addNoticeImageET;
    private ImageView noticeImageView,cancelImage;
    private Button addNoticeBTN;
    private Uri imageUri;
    private String checker = "";
    private DatabaseReference noticeRef;
    private ProgressDialog loadingBar;
    private String userID,noticeTitle,noticeDetails,
            saveCurrtenDate,saveCurrentTime,productRandomKey;
    private String downloadimageURL = "";
    private StorageReference noticeImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        init();

        onAddNoticeImageClicked();

        onCancelImageClicked();

        onAddNoticeBTNClicked();

    }

    private void onAddNoticeBTNClicked() {

        addNoticeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                noticeTitle = noticeTitleET.getText().toString();
                noticeDetails = noticeDetailsET.getText().toString();


                DateAndTimePicker();


                if(checker.equals("clicked"))
                {
                    uploadInfoWithImage();
                }
                else
                {
                    uploadInfo();
                }
            }
        });
    }

    private void DateAndTimePicker() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrtenDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrtenDate + saveCurrentTime;
    }

    private void uploadInfo() {

        if(noticeTitle.equals(""))
        {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
        }
        else if(noticeDetails.equals(""))
        {
            Toast.makeText(this, "Please write about something on "+noticeTitle+ "topic", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Loading();

            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("nID",productRandomKey);
            productMap.put("Date",saveCurrtenDate);
            productMap.put("Time",saveCurrentTime);
            productMap.put("Details",noticeDetails);
            productMap.put("Title",noticeTitle);
            productMap.put("Image",downloadimageURL);

            noticeRef.child(productRandomKey).updateChildren(productMap).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(AddNoticeActivity.this, "Notice Added Successfully.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                startActivity(new Intent(AddNoticeActivity.this,TeacherHomeActivity.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNoticeActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            });

        }




    }

    private void Loading() {
        loadingBar.setTitle("Uploading Notice");
        loadingBar.setMessage("Please wait until the notice is successfully upload.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    private void uploadInfoWithImage() {

        if(noticeTitle.equals(""))
        {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
        }
        else if(noticeDetails.equals(""))
        {
            Toast.makeText(this, "Please write about something on "+noticeTitle+ "topic", Toast.LENGTH_SHORT).show();
        }
        else if(checker == "clicked"){

            Loading();

            uploadImage();
        }
    }

    private void uploadImage() {

        final StorageReference filePath = noticeImageRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNoticeActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddNoticeActivity.this, "Notice Image Uploaded Successfully.", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadimageURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadimageURL = task.getResult().toString();

                            Toast.makeText(AddNoticeActivity.this, "Notice image url got successfully.", Toast.LENGTH_SHORT).show();

                            SaveNoticeInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveNoticeInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("nID",productRandomKey);
        productMap.put("Date",saveCurrtenDate);
        productMap.put("Time",saveCurrentTime);
        productMap.put("Details",noticeDetails);
        productMap.put("Title",noticeTitle);
        productMap.put("Image",downloadimageURL);

        noticeRef.child(productRandomKey).updateChildren(productMap).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AddNoticeActivity.this, "Notice Added Successfully.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            startActivity(new Intent(AddNoticeActivity.this,TeacherHomeActivity.class));
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNoticeActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });
    }


    private void onCancelImageClicked() {

        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageUri = null;
                checker = "";
                noticeImageView.setVisibility(View.INVISIBLE);
                cancelImage.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void onAddNoticeImageClicked() {

        addNoticeImageET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checker = "clicked";

                CropImage.activity(imageUri)
                        .start(AddNoticeActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            noticeImageView.setVisibility(View.VISIBLE);
            cancelImage.setVisibility(View.VISIBLE);
            noticeImageView.setImageURI(imageUri);
        }
        else {
            checker = "";
            Toast.makeText(this, "Error, Try Again...", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        noticeTitleET = findViewById(R.id.noticeTitleET);
        noticeDetailsET = findViewById(R.id.noticeDetailsET);
        noticeImageView = findViewById(R.id.noticeImageView);
        addNoticeImageET = findViewById(R.id.addnoticeimageET);
        addNoticeBTN = findViewById(R.id.addNoticeBTN);
        cancelImage = findViewById(R.id.cancelImage);

        loadingBar = new ProgressDialog(this);
        noticeImageRef = FirebaseStorage.getInstance().getReference().child("Notice Pictures");
        noticeRef = FirebaseDatabase.getInstance().getReference().child("Notices");
    }
}
