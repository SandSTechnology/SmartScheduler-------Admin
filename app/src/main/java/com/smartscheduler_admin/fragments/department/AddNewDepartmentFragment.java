package com.smartscheduler_admin.fragments.department;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smartscheduler_admin.R;

import java.util.Locale;
import java.util.Objects;

public class AddNewDepartmentFragment extends Fragment {
    private EditText department_name;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    int count=0;
    boolean isCountingDone=false;

    public AddNewDepartmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_department, container, false);
        department_name = view.findViewById(R.id.departmentName);
        CardView submitData = view.findViewById(R.id.submitCard);

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        submitData.setOnClickListener(v -> {
            String name = department_name.getText().toString().trim();

            if (name.equals("")) {
                Toast.makeText(getContext(), "Add Department Name", Toast.LENGTH_SHORT).show();
                return;
            }

            CanAddThisDepartment(name);
        });

        getScheduleNumber();
        return view;
    }

    private void CanAddThisDepartment(String departmentName) {
        DatabaseReference newRef = myRef.getRef();

        newRef.child("Departments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot s :
                            snapshot.getChildren()) {
                        if (s.child("NAME").exists() && !Objects.equals(s.child("NAME").getValue(String.class), ""))
                        {
                            if (departmentName.equalsIgnoreCase(s.child("NAME").getValue(String.class)))
                            {
                                Toast.makeText(getContext(), "Department Already Exists", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    SaveDepartment();
                }
                else
                    SaveDepartment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SaveDepartment() {
        DatabaseReference newRef = myRef.child("Departments").child((++count)+"");

        newRef.child("ID").setValue(count);
        newRef.child("NAME").setValue(department_name.getText().toString().toUpperCase(Locale.getDefault()));

        Toast.makeText(getContext(), "Department Added", Toast.LENGTH_SHORT).show();

        department_name .setText("");
    }

    private void getScheduleNumber(){
        DatabaseReference newRef = myRef.getRef();
        newRef.child("Departments").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isCountingDone = true;
                if (snapshot.exists()) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        count = Integer.parseInt(s.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}