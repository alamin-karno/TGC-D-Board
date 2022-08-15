package com.teamblank.tgcdboard.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.teamblank.tgcdboard.R;

public class SyllabusActivity extends AppCompatActivity {

    public static final String FACULTY = "Faculty";
    public static final String BA = "Bachelor of Arts (Honours)";
    public static final String BSS = "Bachelor of Social Science";
    public static final String BS = "Bachelor of Science";
    public static final String BBS = "Bachelor of Business Studies";
    public static final String BLES = "Bachelor of Life and Earth Science";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
    }

    public void back(View view) {
        finish();
    }



    private void intentNewActivity(String facultyValue) {
        Intent intent = new Intent(SyllabusActivity.this,DepartmentActivity.class);
        intent.putExtra(FACULTY,facultyValue);
        startActivity(intent);
    }

    public void BA(View view) {
        intentNewActivity(BA);
    }

    public void BSS(View view) {
        intentNewActivity(BSS);
    }

    public void BS(View view) {
        intentNewActivity(BS);
    }

    public void BBS(View view) {
        intentNewActivity(BBS);
    }

    public void BLES(View view) {
        intentNewActivity(BLES);
    }
}