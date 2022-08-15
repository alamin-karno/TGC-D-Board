package com.teamblank.tgcdboard.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamblank.tgcdboard.R;
import com.teamblank.tgcdboard.adapter.NoticeRecyclerAdapter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;



public class DetailsNoticeActivity extends AppCompatActivity {

    private TextView noticeTitleTv,noticeDateTv,noticeTimeTV,noticeDetailsTV,downloadImageTv;
    ImageView doc_ImageView;
    private String nID;
    private DatabaseReference databaseReference;
    private String title,date,time,details,document;

    OutputStream outputStream =null;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_notice);

        init();

        permissionCheek();

        onDownloadImageClicked();
    }

    private void Loading() {

        loadingBar.setTitle("Notice Updating...");
        loadingBar.setMessage("Please wait until all notice update successfully.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    private void onDownloadImageClicked() {

        downloadImageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                @NonNull Bitmap bitmap;
                bitmap = ((BitmapDrawable) doc_ImageView.getDrawable()).getBitmap();

                if(bitmap == null){
                    Toast.makeText(DetailsNoticeActivity.this, "Developing...", Toast.LENGTH_SHORT).show();
                }
                else {

                    File filepath = Environment.getExternalStorageDirectory();
                    File dir = new File(filepath.getAbsolutePath()+"/TCG Notice/");
                    dir.mkdir();
                    File file = new File(dir,System.currentTimeMillis()+".jpg");

                    try {
                        outputStream = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);

                    try {
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    private void permissionCheek() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE },1);
        }
    }

    private void init() {


        noticeTitleTv = findViewById(R.id.noticeTitleTV);
        noticeDetailsTV =  findViewById(R.id.noticeDetailsTV);
        noticeDateTv =  findViewById(R.id.noticeDateTV);
        noticeTimeTV = findViewById(R.id.noticeTimeTv11);
        doc_ImageView = findViewById(R.id.imageViewLayout);
        downloadImageTv = findViewById(R.id.downloadImageTV);

        loadingBar = new ProgressDialog(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        nID = getIntent().getStringExtra(NoticeRecyclerAdapter.NOTICE_ID);

        if(!nID.isEmpty()){
            LoadAllData();
        }
    }

    private void LoadAllData() {

        Loading();

        DatabaseReference noticeData = databaseReference.child("Notices").child(nID);
        noticeData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    title = dataSnapshot.child("Title").getValue().toString();
                    details = dataSnapshot.child("Details").getValue().toString();
                    date = dataSnapshot.child("Date").getValue().toString();
                    time = dataSnapshot.child("Time").getValue().toString();
                    document = dataSnapshot.child("Image").getValue().toString();

                    if(!title.isEmpty() && !details.isEmpty() && !date.isEmpty() && !time.isEmpty()){

                        noticeTitleTv.setText(title);
                        noticeDetailsTV.setText(details);
                        noticeTimeTV.setText(time);
                        noticeDateTv.setText(date);
                        loadingBar.dismiss();
                    }
                    if(!document.isEmpty()){
                        Glide.with(DetailsNoticeActivity.this).load(document).into(doc_ImageView);
                        loadingBar.dismiss();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailsNoticeActivity.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });
    }
}