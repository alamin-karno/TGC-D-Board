package com.teamblank.tgcdboard.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.teamblank.tgcdboard.R;
import com.teamblank.tgcdboard.adapter.TeacherListAdapter;
import com.teamblank.tgcdboard.model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherListActivity extends AppCompatActivity {

    private EditText teacherSearchET;
    private RecyclerView teacherRecyclerView;
    private TeacherListAdapter teacherListAdapter;
    private List<Teacher> teacherList;
    private DatabaseReference databaseReference;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        init();

        Loading();

        searchTeacher();

        loadTeacherList();

    }


    private void loadTeacherList() {

        if(teacherSearchET.getText().toString().isEmpty()){

            DatabaseReference teacherRef = databaseReference.child("Teacher List");

            teacherRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    teacherList.clear();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Teacher teacher = snapshot.getValue(Teacher.class);
                            teacherList.add(teacher);
                            teacherListAdapter.notifyDataSetChanged();
                        }

                        loadingBar.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(TeacherListActivity.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            });
        }

    }

    private void searchTeacher() {

        teacherSearchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                searchByTeacherName(charSequence.toString().toLowerCase());

                if(teacherSearchET.getText().toString().isEmpty()){
                    loadTeacherList();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchByTeacherName(String teacherName) {

        Query query = databaseReference.child("Teacher List").orderByChild("tSearch").startAt(teacherName+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Teacher teacher = snapshot.getValue(Teacher.class);
                        teacherList.add(teacher);
                        teacherListAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TeacherListActivity.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Loading() {

        loadingBar.setTitle("Teacher List Updating...");
        loadingBar.setMessage("Please wait until all list update successfully.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    private void init() {
        teacherSearchET = findViewById(R.id.teacherSearchET);
        teacherRecyclerView = findViewById(R.id.teacherRecyclerView);
        loadingBar = new ProgressDialog(this);
        teacherRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        teacherList = new ArrayList<>();
        teacherListAdapter = new TeacherListAdapter(this,teacherList);
        teacherRecyclerView.setAdapter(teacherListAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void back(View view) {
        finish();
    }
}