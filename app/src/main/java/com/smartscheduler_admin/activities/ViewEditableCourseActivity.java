package com.smartscheduler_admin.activities;

import android.os.Bundle;
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
import com.smartscheduler_admin.model.CourseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ViewEditableCourseActivity extends AppCompatActivity {
    private EditText course_name;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    List<String> CreditHours = new ArrayList<>();
    Spinner CreditHoursSpinner;
    String ID = "";

    private Spinner SemesterSpinner;
    private Spinner DepartmentSpinner;
    private ArrayAdapter<String> adapterSemester;
    private ArrayAdapter<String> adapterDepartment;

    ArrayList<String> SemesterList = new ArrayList<>();
    ArrayList<String> DepartmentList = new ArrayList<>();

    String DepartmentName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_editable_course);

        CourseModel model = (CourseModel) getIntent().getSerializableExtra("course");

        String CourseName = "";
        String CreditHour = "";
        String Semester = "";

        SemesterSpinner = findViewById(R.id.allSemesterSpinner);
        DepartmentSpinner = findViewById(R.id.allDepartmentSpinner);

        if (model.getID() != null && !model.getID().equals(""))
            ID = model.getID();
        if (model.getCOURSE_NAME() != null && !model.getCOURSE_NAME().equals(""))
            CourseName = model.getCOURSE_NAME();
        if (model.getCREDIT_HOUR() != null && !model.getCREDIT_HOUR().equals(""))
            CreditHour = model.getCREDIT_HOUR();
        if (model.getDEPARTMENT() != null && !model.getDEPARTMENT().equals(""))
            DepartmentName = model.getDEPARTMENT();
        if (model.getSEMESTER() != null && !model.getSEMESTER().equals(""))
            Semester = model.getSEMESTER();

        CreditHoursSpinner = findViewById(R.id.allCreditHoursSpinner);

        course_name = findViewById(R.id.CourseName);

        CardView updateData = findViewById(R.id.updateCard);
        CardView deleteData = findViewById(R.id.deleteCard);

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Semester List
        SemesterList.add("1");
        SemesterList.add("2");
        SemesterList.add("3");
        SemesterList.add("4");
        SemesterList.add("5");
        SemesterList.add("6");
        SemesterList.add("7");
        SemesterList.add("8");

        ArrayAdapter<String> creditHoursAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CreditHours);
        creditHoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CreditHoursSpinner.setAdapter(creditHoursAdapter);

        //Semester Adapter
        adapterSemester = new ArrayAdapter<>(ViewEditableCourseActivity.this, android.R.layout.simple_spinner_item, SemesterList);
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SemesterSpinner.setAdapter(adapterSemester);

        //Department Adapter
        adapterDepartment = new ArrayAdapter<>(ViewEditableCourseActivity.this, android.R.layout.simple_spinner_item, DepartmentList);
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DepartmentSpinner.setAdapter(adapterDepartment);

        myRef.child("Departments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String depName = "";
                    if (areaSnapshot.child("NAME").exists())
                        depName = areaSnapshot.child("NAME").getValue(String.class);
                    DepartmentList.add(depName);
                }
                DepartmentSpinner.setAdapter(adapterDepartment);

                DepartmentSpinner.setSelection(adapterDepartment.getPosition(DepartmentName));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        for (int i = 0; i < 6; i++) {
            CreditHours.add(i + 1 + "");
        }

        CreditHoursSpinner.setAdapter(creditHoursAdapter);

        course_name.setText(CourseName);

        CreditHoursSpinner.setSelection(creditHoursAdapter.getPosition(CreditHour));
        SemesterSpinner.setSelection(adapterSemester.getPosition(Semester));

        updateData.setOnClickListener(v -> {
            String newCourseName = "";
            String department = DepartmentSpinner.getSelectedItem().toString().toUpperCase(Locale.ROOT);
            String semester = SemesterSpinner.getSelectedItem().toString();
            String creditHour = CreditHoursSpinner.getSelectedItem().toString();

            if (course_name.getText() != null && !course_name.getText().toString().equals(""))
                newCourseName = course_name.getText().toString().trim().toUpperCase(Locale.ROOT);

            if (newCourseName.equals("")) {
                Toast.makeText(this, "Add Course Name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (creditHour.equals("")) {
                Toast.makeText(this, "Please Select Credit Hour", Toast.LENGTH_SHORT).show();
                return;
            }

            CanAddThisCourse(newCourseName, department, semester, creditHour);
        });
        deleteData.setOnClickListener(view -> myRef.child("Courses").child(ID).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ViewEditableCourseActivity.this, "Course Deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ViewEditableCourseActivity.this, "Unable to delete Course", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void CanAddThisCourse(String courseName, String department, String semester, String creditHour) {
        DatabaseReference newRef = myRef.getRef();

        newRef.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        String CourseName = "";
                        String Department = "";
                        String Semester = "";

                        if (s.child("SEMESTER").exists() && !Objects.equals(s.child("SEMESTER").getValue(String.class), "")) {
                            Semester = s.child("SEMESTER").getValue(String.class).trim();
                        }

                        if (s.child("DEPARTMENT").exists() && !Objects.equals(s.child("DEPARTMENT").getValue(String.class), "")) {
                            Department = s.child("DEPARTMENT").getValue(String.class).trim();
                        }

                        if (s.child("COURSE_NAME").exists() && !Objects.equals(s.child("COURSE_NAME").getValue(String.class), "")) {
                            CourseName = s.child("COURSE_NAME").getValue(String.class).trim();
                        }

                        try {
                            courseName.trim();
                            department.trim();
                            semester.trim();

                            CourseName.trim();
                            Department.trim();
                            Semester.trim();
                        } catch (Exception ignored) {
                        }


                        if (!Objects.equals(s.getKey(), ID)) {
                            if (courseName.equalsIgnoreCase(CourseName) &&
                                    department.equalsIgnoreCase(Department) &&
                                    semester.equalsIgnoreCase(Semester)) {
                                Toast.makeText(ViewEditableCourseActivity.this, "Course Already Exists", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    UpdateCourse();
                } else
                    UpdateCourse();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void UpdateCourse() {
        DatabaseReference newRef = myRef.child("Courses").child((ID) + "");

        newRef.child("COURSE_NAME").setValue(course_name.getText().toString().trim().toUpperCase(Locale.getDefault()));
        newRef.child("CREDIT_HOUR").setValue(CreditHoursSpinner.getSelectedItem().toString());
        newRef.child("DEPARTMENT").setValue(DepartmentSpinner.getSelectedItem().toString());
        newRef.child("SEMESTER").setValue(SemesterSpinner.getSelectedItem().toString());

        Toast.makeText(this, "Course Updated", Toast.LENGTH_SHORT).show();
    }
}