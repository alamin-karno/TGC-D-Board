package com.teamblank.tgcdboard.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamblank.tgcdboard.R;
import com.teamblank.tgcdboard.adapter.NoticeRecyclerAdapter;
import com.teamblank.tgcdboard.model.Notice;

import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {

    private RecyclerView noticeRecyclerView;
    private List<Notice> noticeList;
    private NoticeRecyclerAdapter adapter;
    private DatabaseReference databaseReference;
    private LinearLayoutManager layoutManager;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        init();

        Loading();

        getDataFromFirebase();
    }

    private void Loading() {

        loadingBar.setTitle("Notice Updating...");
        loadingBar.setMessage("Please wait until all notice update successfully.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    private void getDataFromFirebase() {


        DatabaseReference noticeData = databaseReference.child("Notices");
        noticeData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noticeList.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot noticeData:dataSnapshot.getChildren()){
                        Notice notice = noticeData.getValue(Notice.class);
                        noticeList.add(notice);
                    }
                    adapter.notifyDataSetChanged();
                    loadingBar.dismiss();
                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(NoticeActivity.this, "No notice found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NoticeActivity.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });
    }

    private void init() {

        noticeRecyclerView = findViewById(R.id.noticeRecyclerView);
        loadingBar = new ProgressDialog(this);
        noticeList = new ArrayList<>();
        adapter = new NoticeRecyclerAdapter(getApplicationContext(),noticeList);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        noticeRecyclerView.setLayoutManager(layoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        noticeRecyclerView.setAdapter(adapter);
    }

    public void back(View view) {
        finish();
    }
}
