package com.smartscheduler_admin.fragments.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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

public class AddNewCourseFragment extends Fragment {
    private EditText course_name;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    int count = 0;
    boolean isCountingDone = false;
    List<String> CreditHours = new ArrayList<>();
    Spinner CreditHoursSpinner;
    private Spinner SemesterSpinner;
    private Spinner DepartmentSpinner;
    private ArrayAdapter<String> adapterSemester;
    private ArrayAdapter<String> adapterDepartment;

    ArrayList<String> SemesterList = new ArrayList<>();
    ArrayList<String> DepartmentList = new ArrayList<>();

    public AddNewCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_course, container, false);

        CreditHoursSpinner = view.findViewById(R.id.allCreditHoursSpinner);

        course_name = view.findViewById(R.id.CourseName);

        CardView submitData = view.findViewById(R.id.submitCard);
        SemesterSpinner = view.findViewById(R.id.allSemesterSpinner);
        DepartmentSpinner = view.findViewById(R.id.allDepartmentSpinner);

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

        //Credit Hour Adapter
        ArrayAdapter<String> creditHoursAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, CreditHours);
        creditHoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CreditHoursSpinner.setAdapter(creditHoursAdapter);

        //Semester Adapter
        adapterSemester = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, SemesterList);
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SemesterSpinner.setAdapter(adapterSemester);

        //Department Adapter
        adapterDepartment = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, DepartmentList);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        for (int i = 0; i < 4; i++) {
            CreditHours.add(i + 1 + "");
        }

        CreditHoursSpinner.setAdapter(creditHoursAdapter);

        submitData.setOnClickListener(v -> {
            String CourseName = "";
            String department = DepartmentSpinner.getSelectedItem().toString().toUpperCase(Locale.ROOT);
            String semester = SemesterSpinner.getSelectedItem().toString();
            String creditHour = CreditHoursSpinner.getSelectedItem().toString();

            if (course_name.getText() != null && !course_name.getText().toString().equals(""))
                CourseName = course_name.getText().toString().trim().toUpperCase(Locale.ROOT);

            if (CourseName.equals("")) {
                Toast.makeText(getContext(), "Add Course Name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (creditHour.equals("")) {
                Toast.makeText(getContext(), "Please Select Credit Hour", Toast.LENGTH_SHORT).show();
                return;
            }

            CanAddThisCourse(CourseName,department,semester,creditHour);
        });

        getScheduleNumber();

        return view;
    }

    private void CanAddThisCourse(String courseName,String department,String semester,String creditHour) {
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
                        }catch (Exception ignored){}

                        if (courseName.equalsIgnoreCase(CourseName) &&
                                department.equalsIgnoreCase(Department) &&
                                semester.equalsIgnoreCase(Semester)) {
                            Toast.makeText(getContext(), "Course Already Exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    SaveCourse();
                } else
                    SaveCourse();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SaveCourse() {
        DatabaseReference newRef = myRef.child("Courses").child((++count) + "");

        newRef.child("COURSE_NAME").setValue(course_name.getText().toString().toUpperCase(Locale.getDefault()));
        newRef.child("CREDIT_HOUR").setValue(CreditHoursSpinner.getSelectedItem().toString());
        newRef.child("DEPARTMENT").setValue(DepartmentSpinner.getSelectedItem().toString());
        newRef.child("SEMESTER").setValue(SemesterSpinner.getSelectedItem().toString());

        Toast.makeText(getContext(), "Course Added", Toast.LENGTH_SHORT).show();

        course_name.setText("");
        CreditHoursSpinner.setSelection(0);
        DepartmentSpinner.setSelection(0);
        SemesterSpinner.setSelection(0);
    }

    private void getScheduleNumber() {
        DatabaseReference newRef = myRef.getRef();
        newRef.child("Courses").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
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
}