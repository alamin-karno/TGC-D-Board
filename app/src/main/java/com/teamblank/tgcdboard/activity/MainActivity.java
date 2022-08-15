package com.teamblank.tgcdboard.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.teamblank.tgcdboard.R;

public class MainActivity extends AppCompatActivity {

    private LinearLayout viewNoticeLL,viewRoutineLL,aboutLL,logoutLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        autoLogin();

        onNoticeClicked();

        onRoutineClicked();

        onAboutClicked();

        onLogOutClicked();
    }

    private void autoLogin() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){

            Intent intent = new Intent(MainActivity.this,TeacherHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    private void onLogOutClicked() {

        logoutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MainActivity.this);
                deleteDialog.setTitle("Warning!");
                deleteDialog.setMessage("Are you sure to login as Admin?");

                deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {

                            startActivity(new Intent(MainActivity.this,TeacherLoginActivity.class));
                            finish();

                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, "Exception: "+e, Toast.LENGTH_SHORT).show();
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
        });
    }

    private void onAboutClicked() {
        aboutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,AboutUsActivity.class));
            }
        });
    }

    private void onRoutineClicked() {
        viewRoutineLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,RoutineActivity.class));
            }
        });
    }

    private void onNoticeClicked() {
        viewNoticeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,NoticeActivity.class));
            }
        });
    }

    private void init() {
        viewNoticeLL = findViewById(R.id.viewNoticeLL);
        viewRoutineLL = findViewById(R.id.viewRoutineLL);
        aboutLL = findViewById(R.id.aboutLL);
        logoutLL = findViewById(R.id.logoutLL);
    }

    public void teacherList(View view) {

            startActivity(new Intent(MainActivity.this, TeacherListActivity.class));

    }

    public void staffList(View view) {
        Toast.makeText(this, "Developing...", Toast.LENGTH_SHORT).show();
    }

    public void Syllabus(View view) {

        startActivity(new Intent(MainActivity.this, SyllabusActivity.class));
    }
}
