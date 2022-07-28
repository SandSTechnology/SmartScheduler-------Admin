package com.smartscheduler_admin.activities;

import android.os.Bundle;
import android.widget.EditText;
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
import com.smartscheduler_admin.model.DepartmentsModel;

import java.util.Locale;
import java.util.Objects;

public class ViewEditableDepartmentActivity extends AppCompatActivity {
    private EditText department_name;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_editable_department);

        department_name = findViewById(R.id.departmentName);

        CardView updateData = findViewById(R.id.updateCard);
        CardView deleteData = findViewById(R.id.deleteCard);

        DepartmentsModel model = (DepartmentsModel) getIntent().getSerializableExtra("department");

        String DepartmentName = "";

        if (model.getID() != null && !model.getID().equals(""))
            ID = model.getID();
        if (model.getDEPARTMENT() != null && !model.getDEPARTMENT().equals(""))
            DepartmentName = model.getDEPARTMENT();

        department_name.setText(DepartmentName);

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        updateData.setOnClickListener(v -> {
            String name = department_name.getText().toString().trim();

            if (name.equals("")) {
                Toast.makeText(this, "Add Department Name", Toast.LENGTH_SHORT).show();
                return;
            }

            CanAddThisDepartment(name);
        });
        deleteData.setOnClickListener(view -> myRef.child("Departments").child(ID).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ViewEditableDepartmentActivity.this, "Department Deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ViewEditableDepartmentActivity.this, "Unable to delete Department", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void CanAddThisDepartment(String departmentName) {
        DatabaseReference newRef = myRef.getRef();

        newRef.child("Departments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot s :
                            snapshot.getChildren()) {
                        if (s.child("NAME").exists() && !Objects.equals(s.child("NAME").getValue(String.class), "")) {
                            if (!Objects.equals(s.getKey(), ID))
                                if (departmentName.equalsIgnoreCase(s.child("NAME").getValue(String.class))) {
                                    Toast.makeText(ViewEditableDepartmentActivity.this, "Department Already Exists", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                        }
                    }
                    UpdateDepartment();
                } else
                    UpdateDepartment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void UpdateDepartment() {
        DatabaseReference newRef = myRef.child("Departments").child(ID);

        newRef.child("ID").setValue(ID);
        newRef.child("NAME").setValue(department_name.getText().toString().toUpperCase(Locale.getDefault()));

        Toast.makeText(this, "Department Updated", Toast.LENGTH_SHORT).show();
    }
}