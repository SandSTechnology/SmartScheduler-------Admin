package com.smartscheduler_admin.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smartscheduler_admin.R;
import com.smartscheduler_admin.model.FacultyModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ViewEditableFacultyActivity extends AppCompatActivity {
    CardView updateCard, deleteCard;
    private EditText teacher_name, teacher_short_name, teacher_email, teacher_mobileNumber;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    int count = 0;
    List<String> departments = new ArrayList<>();
    List<String> semesters = new ArrayList<>();
    List<String> courses = new ArrayList<>();
    FacultyModel facultyModel;

    String name = "";
    String email = "";
    String short_name = "";
    String mobileNum = "";
    String department;
    String semester;
    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_editable_faculty);

        facultyModel = (FacultyModel) getIntent().getSerializableExtra("faculty");

        count = Integer.parseInt(facultyModel.getID());

        Spinner DepartmentSpinner = findViewById(R.id.allDepartmentSpinner);
        Spinner SemesterSpinner = findViewById(R.id.allSemesterSpinner);
        Spinner CoursesSpinner = findViewById(R.id.allCoursesSpinner);

        teacher_name = findViewById(R.id.Name);
        teacher_email = findViewById(R.id.Email);
        teacher_short_name = findViewById(R.id.ShortName);
        teacher_mobileNumber = findViewById(R.id.MobileNumber);
        updateCard = findViewById(R.id.updateCard);
        deleteCard = findViewById(R.id.deleteCard);

        teacher_name.setText(facultyModel.getNAME());
        teacher_email.setText(facultyModel.getEMAIL());
        teacher_short_name.setText(facultyModel.getSHORT_NAME());
        teacher_mobileNumber.setText(facultyModel.getMOBILE_NUMBER());

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        ArrayAdapter<String> departmentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        departmentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DepartmentSpinner.setAdapter(departmentsAdapter);

        ArrayAdapter<String> SemestersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, semesters);
        SemestersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SemesterSpinner.setAdapter(SemestersAdapter);

        ArrayAdapter<String> CoursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        CoursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CoursesSpinner.setAdapter(CoursesAdapter);

        myRef.child("Departments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                        String areaName = "";
                        if (areaSnapshot.child("NAME").exists())
                            areaName = areaSnapshot.child("NAME").getValue(String.class);
                        departments.add(areaName);
                    }

                DepartmentSpinner.setAdapter(departmentsAdapter);
                DepartmentSpinner.setSelection(departmentsAdapter.getPosition(facultyModel.getDEPARTMENT()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef.child("Courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                        String courseName = "";
                        if (courseSnapshot.child("COURSE_NAME").exists())
                            courseName = courseSnapshot.child("COURSE_NAME").getValue(String.class);
                        courses.add(courseName);
                    }

                CoursesSpinner.setAdapter(CoursesAdapter);
                CoursesSpinner.setSelection(CoursesAdapter.getPosition(facultyModel.getSUBJECT()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        for (int i = 0; i < 8; i++) {
            semesters.add(i + 1 + "");
        }
        SemesterSpinner.setAdapter(SemestersAdapter);
        SemesterSpinner.setSelection(SemestersAdapter.getPosition(facultyModel.getSEMESTER()));

        updateCard.setOnClickListener(v -> {
            if (teacher_name.getText() != null && !teacher_name.getText().toString().equals(""))
                name = teacher_name.getText().toString();
            if (teacher_email.getText() != null && !teacher_email.getText().toString().equals(""))
                email = teacher_email.getText().toString();

            if (teacher_short_name.getText() != null && !teacher_short_name.getText().toString().equals(""))
                short_name = teacher_name.getText().toString();
            if (teacher_mobileNumber.getText() != null && !teacher_mobileNumber.getText().toString().equals(""))
                mobileNum = teacher_mobileNumber.getText().toString();
            department = DepartmentSpinner.getSelectedItem().toString();
            semester = SemesterSpinner.getSelectedItem().toString();
            subject = CoursesSpinner.getSelectedItem().toString();

            if (name.equals("")) {
                Toast.makeText(this, "Add Teacher Name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.equals("")) {
                Toast.makeText(this, "Add Teacher Email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "invalid Email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (short_name.equals("")) {
                Toast.makeText(this, "Add Teacher Short Name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mobileNum.equals("")) {
                Toast.makeText(this, "Add Teacher Mobile Number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mobileNum.length() < 10) {
                Toast.makeText(this, "invalid Mobile Number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (department.equals("")) {
                Toast.makeText(this, "Please Select Department", Toast.LENGTH_SHORT).show();
                return;
            }

            if (semester.equals("")) {
                Toast.makeText(this, "Please Select Semester", Toast.LENGTH_SHORT).show();
                return;
            }

            if (subject.equals("")) {
                Toast.makeText(this, "Please Select Course", Toast.LENGTH_SHORT).show();
                return;
            }

            CheckAvailability();
        });

        deleteCard.setOnClickListener(view -> myRef.child("Faculty").child(count + "").removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ViewEditableFacultyActivity.this, "Faculty Deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ViewEditableFacultyActivity.this, "Unable to delete Faculty", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void CheckAvailability() {

        myRef.child("Faculty").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean available = true;
                if (snapshot.exists())
                {
                    for (DataSnapshot s :
                            snapshot.getChildren()) {
                        String Email = s.child("EMAIL").getValue(String.class);

                        if (Objects.equals(Email, email) && !s.getKey().equals(count + ""))
                            available = false;

                    }
                }

                if (available)
                {
                    HashMap<String,Object> data = new HashMap<>();
                    data.put("NAME",name);
                    data.put("EMAIL",email);
                    data.put("SHORT_NAME",short_name);
                    data.put("MOBILE_NUMBER",mobileNum);
                    data.put("SEMESTER",semester.toUpperCase(Locale.ROOT));
                    data.put("DEPARTMENT",department.toUpperCase(Locale.ROOT));
                    data.put("SUBJECT",subject.toUpperCase(Locale.ROOT));

                    myRef.child("Faculty").child((count) + "").updateChildren(data);

                    Toast.makeText(ViewEditableFacultyActivity.this, "Faculty Updated", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ViewEditableFacultyActivity.this, "Faculty Email Already Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}