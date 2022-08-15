package com.teamblank.tgcdboard.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamblank.tgcdboard.R;
import com.teamblank.tgcdboard.model.Admin;

public class TeacherLoginActivity extends AppCompatActivity {

    private EditText userNameET,passwordET;
    private Button adminLoginBTN;
    private String email,password;
    private ProgressDialog loadingBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        init();



        adminLoginClicked();
    }

    private void adminLoginClicked() {

        adminLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = userNameET.getText().toString();
                password = passwordET.getText().toString();

                if(email.equals("")){
                    Toast.makeText(TeacherLoginActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                }
                else if(password.equals(""))
                {
                    Toast.makeText(TeacherLoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    adminLogin(email,password);
                }

            }
        });
    }

    private void adminLogin(final String email, final String password) {

        Loading();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Intent intent = new Intent(TeacherLoginActivity.this,TeacherHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    loadingBar.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TeacherLoginActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });

    }



    private void Loading() {
        loadingBar.setTitle("Login in to your account...");
        loadingBar.setMessage("Please wait until the account is successfully login.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    private void init() {
        userNameET = findViewById(R.id.userNameET);
        passwordET = findViewById(R.id.passwordET);
        adminLoginBTN = findViewById(R.id.adminloginBTN);

        loadingBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }


    public void stduntlogin(View view) {
        startActivity(new Intent(TeacherLoginActivity.this, MainActivity.class));
    }
}
