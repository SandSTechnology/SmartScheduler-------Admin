package com.smartscheduler_admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smartscheduler_admin.R;
import com.smartscheduler_admin.adapter_recycler_view.AdapterAllFaculty;
import com.smartscheduler_admin.model.FacultyModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ViewAllFacultiesActivity extends AppCompatActivity {
    ArrayList<FacultyModel> list;
    AdapterAllFaculty adapter;
    RecyclerView recyclerView;
    View NoRecordFoundView;
    DatabaseReference myRef;
    ProgressBar loadingBar;
    FirebaseUser firebaseUser;
    ValueEventListener allValueListener=null;
    FloatingActionButton AddNewFacultyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_faculities);

        final Toolbar toolbar = findViewById(R.id.default_toolBar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setTitle("Faculty");
        }

        NoRecordFoundView = findViewById(R.id.noRcdFnd);
        NoRecordFoundView.setVisibility(View.GONE);
        loadingBar = findViewById(R.id.loadingBar);
        AddNewFacultyBtn = findViewById(R.id.AddNewFacultyBtn);

        AddNewFacultyBtn.setOnClickListener(view -> startActivity(new Intent(ViewAllFacultiesActivity.this,AddNewFacultyActivity.class)));

        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.rec);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapter = new AdapterAllFaculty(list, this);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    @Override
    public void onStop() {
        if (allValueListener != null) {
            myRef.removeEventListener(allValueListener);
        }
        super.onStop();
    }

    private void getData() {
        loadingBar.setVisibility(View.VISIBLE);
        myRef.child("Faculty").addValueEventListener(allValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String ID;
                        String NAME= "";
                        String SUBJECT= "";
                        String DEPARTMENT= "";
                        String EMAIL= "";
                        String MOBILE_NUMBER= "";
                        String SEMESTER= "";
                        String SHORT_NAME= "";

                        ID = dataSnapshot.getKey();

                        if (dataSnapshot.hasChild("NAME"))
                            NAME = Objects.requireNonNull(dataSnapshot.child("NAME").getValue()).toString();
                        if (dataSnapshot.hasChild("DEPARTMENT"))
                            DEPARTMENT = Objects.requireNonNull(dataSnapshot.child("DEPARTMENT").getValue()).toString();
                        if (dataSnapshot.hasChild("SUBJECT"))
                            SUBJECT = Objects.requireNonNull(dataSnapshot.child("SUBJECT").getValue()).toString();

                        if (dataSnapshot.hasChild("EMAIL"))
                            EMAIL = Objects.requireNonNull(dataSnapshot.child("EMAIL").getValue()).toString();
                        if (dataSnapshot.hasChild("MOBILE_NUMBER"))
                            MOBILE_NUMBER = Objects.requireNonNull(dataSnapshot.child("MOBILE_NUMBER").getValue()).toString();
                        if (dataSnapshot.hasChild("SEMESTER"))
                            SEMESTER = Objects.requireNonNull(dataSnapshot.child("SEMESTER").getValue()).toString();
                        if (dataSnapshot.hasChild("SHORT_NAME"))
                            SHORT_NAME = Objects.requireNonNull(dataSnapshot.child("SHORT_NAME").getValue()).toString();

                        list.add(new FacultyModel(ID,NAME,SUBJECT,DEPARTMENT,EMAIL,MOBILE_NUMBER,SEMESTER,SHORT_NAME));
                    }
                    if (list.isEmpty()) {
                        if (loadingBar.getVisibility() == View.VISIBLE)
                            loadingBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        NoRecordFoundView.setVisibility(View.VISIBLE);
                    } else {
                        Collections.reverse(list);
                        loadingBar.setVisibility(View.GONE);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    loadingBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    NoRecordFoundView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}