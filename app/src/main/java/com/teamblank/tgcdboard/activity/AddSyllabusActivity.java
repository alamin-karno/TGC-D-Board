package com.teamblank.tgcdboard.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamblank.tgcdboard.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddSyllabusActivity extends AppCompatActivity {

    Spinner facultySpinner,departmentSpinner,yearSpinner;
    Button addSyllabusBTN;
    EditText pdfLinkET;
    private String faculty,department,year,date,time,pdfLink;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_syllabus);

        init();

        loadFacultySpinner();

        loadDepartmentSpinner(R.array.BS);

        onAddSyllabusBTNClicked();

    }

    private void onAddSyllabusBTNClicked() {
        addSyllabusBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pdfLink = pdfLinkET.getText().toString();
                faculty = facultySpinner.getSelectedItem().toString();
                department = departmentSpinner.getSelectedItem().toString();
                year = yearSpinner.getSelectedItem().toString();

                if(facultySpinner.getSelectedItemPosition() == 0){
                    Toast.makeText(AddSyllabusActivity.this, "Please choose a faculty", Toast.LENGTH_SHORT).show();
                }
                else if(departmentSpinner.getSelectedItemPosition() == 0){
                    Toast.makeText(AddSyllabusActivity.this, "Please choose a department", Toast.LENGTH_SHORT).show();
                }
                else if(yearSpinner.getSelectedItemPosition() == 0){
                    Toast.makeText(AddSyllabusActivity.this, "Please choose a year", Toast.LENGTH_SHORT).show();
                }
                else if(pdfLink.isEmpty()){
                      pdfLinkET.setError("Enter pdf link");
                }
                else {
                    uploadPDF();
                }
            }
        });
    }

    private void uploadPDF() {

        String title = department+" ( "+year+" )";

        DateAndTimePicker();

        DatabaseReference syllabusRef = databaseReference.child("Syllabus").child(faculty).child(title);

        HashMap<String,String> syllabusMap = new HashMap<>();
        syllabusMap.put("sTitle",title);
        syllabusMap.put("SPDFLink",pdfLink);
        syllabusMap.put("sFaculty",faculty);
        syllabusMap.put("sDepartment",department);
        syllabusMap.put("sDate",date);
        syllabusMap.put("sTime",time);
        syllabusMap.put("sID",title);

        syllabusRef.setValue(syllabusMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //facultySpinner.setSelection(0);
                    //departmentSpinner.setSelection(0);
                    yearSpinner.setSelection(0);
                    pdfLinkET.setText("");
                    Toast.makeText(AddSyllabusActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddSyllabusActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DateAndTimePicker() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        date = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
        time = currentTime.format(calendar.getTime());


    }

    private void loadFacultySpinner() {

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.facultyList, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        facultySpinner.setAdapter(arrayAdapter);

        facultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                faculty = adapterView.getItemAtPosition(i).toString();
                switch (faculty) {
                    case SyllabusActivity.BA:
                        loadDepartmentSpinner(R.array.BA);
                        break;
                    case SyllabusActivity.BBS:
                        loadDepartmentSpinner(R.array.BBS);
                        break;
                    case SyllabusActivity.BLES:
                        loadDepartmentSpinner(R.array.BLES);
                        break;
                    case SyllabusActivity.BSS:
                        loadDepartmentSpinner(R.array.BSS);
                        break;
                    case SyllabusActivity.BS:
                        loadDepartmentSpinner(R.array.BS);
                        break;
                    case "Choose a  Department":
                        loadDepartmentSpinner(R.array.BS);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadDepartmentSpinner(int departmentList) {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(AddSyllabusActivity.this,departmentList, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(arrayAdapter);

    }


    private void init() {
        facultySpinner = findViewById(R.id.facultySpinner);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        addSyllabusBTN = findViewById(R.id.addSyllabusBTN);
        pdfLinkET = findViewById(R.id.pdfLinkET);


        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
}