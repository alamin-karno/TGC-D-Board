package com.teamblank.tgcdboard.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamblank.tgcdboard.R;
import com.teamblank.tgcdboard.adapter.SyllabusRecyclerAdapter;
import com.teamblank.tgcdboard.model.Syllabus;

import java.util.ArrayList;
import java.util.List;

public class DepartmentActivity extends AppCompatActivity {

    private RecyclerView syllabusRecyclerView;
    private SyllabusRecyclerAdapter syllabusRecyclerAdapter;
    private List<Syllabus> syllabusList;
    private TextView title;
    private ProgressDialog loadingBar;

    DatabaseReference databaseReference;

    private String faculty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        init();

        loadSyllabus();
    }

    private void loadSyllabus() {

        if(faculty != null){

            title.setText(faculty);

            Loading();

            switch (faculty) {
                case SyllabusActivity.BA:
                    loadSpecificSyllabus(SyllabusActivity.BA);
                    break;
                case SyllabusActivity.BBS:
                    loadSpecificSyllabus(SyllabusActivity.BBS);
                    break;
                case SyllabusActivity.BLES:
                    loadSpecificSyllabus(SyllabusActivity.BLES);
                    break;
                case SyllabusActivity.BSS:
                    loadSpecificSyllabus(SyllabusActivity.BSS);
                    break;
                case SyllabusActivity.BS:
                    loadSpecificSyllabus(SyllabusActivity.BS);
                    break;
            }
        }
        else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadSpecificSyllabus(String specificFaculty) {

        DatabaseReference facultyRef = databaseReference.child(specificFaculty);

       facultyRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               syllabusList.clear();
               if(dataSnapshot.exists()){
                   for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                       Syllabus syllabus = snapshot.getValue(Syllabus.class);
                       syllabusList.add(syllabus);
                       syllabusRecyclerAdapter.notifyDataSetChanged();
                       loadingBar.dismiss();
                   }
               }
               else {
                   loadingBar.dismiss();
                   Toast.makeText(DepartmentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               loadingBar.dismiss();
               Toast.makeText(DepartmentActivity.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }

    private void Loading() {

        loadingBar.setTitle("Syllabus Updating...");
        loadingBar.setMessage("Please wait until all syllabus update successfully.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    private void init() {

        faculty = getIntent().getStringExtra(SyllabusActivity.FACULTY);

        title = findViewById(R.id.toolbarTitle);
        syllabusRecyclerView = findViewById(R.id.syllabusRecyclerView);
        syllabusRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        syllabusList = new ArrayList<>();
        syllabusRecyclerAdapter = new SyllabusRecyclerAdapter(syllabusList,this);
        syllabusRecyclerView.setAdapter(syllabusRecyclerAdapter);

        loadingBar = new ProgressDialog(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Syllabus");
    }

    public void back(View view) {
        finish();
    }
}