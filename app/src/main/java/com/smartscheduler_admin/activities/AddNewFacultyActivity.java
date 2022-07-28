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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddNewFacultyActivity extends AppCompatActivity {
    private EditText teacher_name, teacher_short_name, teacher_email, teacher_mobileNumber;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    int count = 0;
    boolean isCountingDone = false;
    List<String> departments = new ArrayList<>();
    List<String> semesters = new ArrayList<>();
    List<String> courses = new ArrayList<>();
    String name = "";
    String email = "";
    String short_name = "";
    String mobileNum = "";
    String department;
    String semester;
    String subject;
    Spinner DepartmentSpinner;
    Spinner SemesterSpinner;
    Spinner CoursesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_faculty);

        DepartmentSpinner = findViewById(R.id.allDepartmentSpinner);
        SemesterSpinner = findViewById(R.id.allSemesterSpinner);
        CoursesSpinner = findViewById(R.id.allCoursesSpinner);

        teacher_name = findViewById(R.id.Name);
        teacher_email = findViewById(R.id.Email);
        teacher_short_name = findViewById(R.id.ShortName);
        teacher_mobileNumber = findViewById(R.id.MobileNumber);

        CardView submitData = findViewById(R.id.submitCard);

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        for (int i = 0; i < 8; i++) {
            semesters.add(i + 1 + "");
        }
        SemesterSpinner.setAdapter(SemestersAdapter);

        submitData.setOnClickListener(v -> {
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

        getScheduleNumber();
    }

    private void getScheduleNumber() {
        DatabaseReference newRef = myRef.getRef();
        newRef.child("Faculty").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isCountingDone = true;
                if (snapshot.exists()) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        count = Integer.parseInt(Objects.requireNonNull(s.getKey()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckAvailability() {
        myRef.child("Faculty").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean available = true;
                if (snapshot.exists()) {
                    for (DataSnapshot s :
                            snapshot.getChildren()) {
                        String Email = s.child("EMAIL").getValue(String.class);

                        if (Objects.equals(Email, email))
                            available = false;

                    }
                }

                if (available) {
                    DatabaseReference newRef = myRef.child("Faculty").child((++count) + "");

                    newRef.child("NAME").setValue(name);
                    newRef.child("EMAIL").setValue(email);
                    newRef.child("SHORT_NAME").setValue(short_name);
                    newRef.child("MOBILE_NUMBER").setValue(mobileNum);
                    newRef.child("SEMESTER").setValue(semester.toUpperCase(Locale.ROOT));
                    newRef.child("DEPARTMENT").setValue(department.toUpperCase(Locale.ROOT));
                    newRef.child("SUBJECT").setValue(subject.toUpperCase(Locale.ROOT));

                    Toast.makeText(AddNewFacultyActivity.this, "Faculty Added", Toast.LENGTH_SHORT).show();

                    teacher_name.setText("");
                    teacher_email.setText("");
                    CoursesSpinner.setSelection(0);
                    SemesterSpinner.setSelection(0);
                    DepartmentSpinner.setSelection(0);
                } else {
                    Toast.makeText(AddNewFacultyActivity.this, "Faculty Email Already Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}