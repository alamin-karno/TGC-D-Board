package com.teamblank.tgcdboard.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teamblank.tgcdboard.R;
import com.teamblank.tgcdboard.adapter.TeacherListAdapter;

public class TeacherHomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
    }

    public void logout(View view) {

        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(TeacherHomeActivity.this);
        deleteDialog.setTitle("Warning!");
        deleteDialog.setMessage("Are you sure to logout?");

        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {

                    FirebaseAuth.getInstance().signOut();

                    startActivity(new Intent(TeacherHomeActivity.this,TeacherLoginActivity.class));
                    finish();

                }catch (Exception e){
                    Toast.makeText(TeacherHomeActivity.this, "Exception: "+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        deleteDialog.show();
    }

    public void addNotice(View view) {

        startActivity(new Intent(TeacherHomeActivity.this,AddNoticeActivity.class));
    }

    public void addRoutine(View view) {

        Toast.makeText(this, "Developing...", Toast.LENGTH_SHORT).show();
    }

    public void attendance(View view) {

        Toast.makeText(this, "Developing...", Toast.LENGTH_SHORT).show();
    }

    public void addTeacher(View view) {

            startActivity(new Intent(TeacherHomeActivity.this, AddTeacherActivity.class));

    }

    public void Add_Syllabus(View view) {
        
        startActivity(new Intent(TeacherHomeActivity.this, AddSyllabusActivity.class));
    }
}
