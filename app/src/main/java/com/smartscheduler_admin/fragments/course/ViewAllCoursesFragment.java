package com.smartscheduler_admin.fragments.course;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smartscheduler_admin.R;
import com.smartscheduler_admin.adapter_recycler_view.AdapterAllCourses;
import com.smartscheduler_admin.adapter_recycler_view.AdapterAllFaculty;
import com.smartscheduler_admin.model.CourseModel;
import com.smartscheduler_admin.model.FacultyModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

public class ViewAllCoursesFragment extends Fragment {
    ArrayList<CourseModel> list;
    AdapterAllCourses adapter;
    RecyclerView recyclerView;
    View NoRecordFoundView;
    DatabaseReference myRef;
    ProgressBar loadingBar;
    FirebaseUser firebaseUser;
    ValueEventListener allValueListener=null;
    SearchView searchView;

    public ViewAllCoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_courses, container, false);

        NoRecordFoundView = view.findViewById(R.id.noRcdFnd);
        NoRecordFoundView.setVisibility(View.GONE);
        loadingBar = view.findViewById(R.id.loadingBar);

        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.rec);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapter = new AdapterAllCourses(list, getActivity(), this);

        recyclerView.setAdapter(adapter);

        searchView = view.findViewById(R.id.SearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() > 0)
                {
                    getFilteredData(newText);
                }
                else
                {
                    getData();
                }
                return false;
            }
        });

        return view;
    }

    private void getFilteredData(String query) {
        loadingBar.setVisibility(View.VISIBLE);
        myRef.child("Courses").addListenerForSingleValueEvent(allValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String ID= "";
                        String COURSE_NAME= "";
                        String CREDIT_HOUR= "";
                        String DEPARTMENT= "";
                        String SEMESTER= "";
                        String FACULTY_NAME = "";
                        String FACULTY_ID = "";

                        ID = dataSnapshot.getKey();

                        if (dataSnapshot.hasChild("COURSE_NAME"))
                            COURSE_NAME = Objects.requireNonNull(dataSnapshot.child("COURSE_NAME").getValue()).toString();
                        if (dataSnapshot.hasChild("CREDIT_HOUR"))
                            CREDIT_HOUR = Objects.requireNonNull(dataSnapshot.child("CREDIT_HOUR").getValue()).toString();
                        if (dataSnapshot.hasChild("DEPARTMENT"))
                            DEPARTMENT = Objects.requireNonNull(dataSnapshot.child("DEPARTMENT").getValue()).toString();
                        if (dataSnapshot.hasChild("SEMESTER"))
                            SEMESTER = Objects.requireNonNull(dataSnapshot.child("SEMESTER").getValue()).toString();
                        if (dataSnapshot.child("FACULTY_NAME").exists())
                            FACULTY_NAME = dataSnapshot.child("FACULTY_NAME").getValue(String.class);
                        if (dataSnapshot.child("FACULTY_ID").exists())
                            FACULTY_ID = dataSnapshot.child("FACULTY_ID").getValue(String.class);
                        if (Objects.requireNonNull(COURSE_NAME.toLowerCase(Locale.ROOT)).contains(query.toLowerCase(Locale.ROOT)))
                        {
                            list.add(new CourseModel(ID,COURSE_NAME,CREDIT_HOUR,DEPARTMENT,SEMESTER,FACULTY_ID,FACULTY_NAME));
                        }
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
                        NoRecordFoundView.setVisibility(View.GONE);
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
        myRef.child("Courses").addListenerForSingleValueEvent(allValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String ID= "";
                        String COURSE_NAME= "";
                        String CREDIT_HOUR= "";
                        String DEPARTMENT= "";
                        String SEMESTER= "";
                        String FACULTY_NAME = "";
                        String FACULTY_ID = "";

                        ID = dataSnapshot.getKey();

                        if (dataSnapshot.hasChild("COURSE_NAME"))
                            COURSE_NAME = Objects.requireNonNull(dataSnapshot.child("COURSE_NAME").getValue()).toString();
                        if (dataSnapshot.hasChild("CREDIT_HOUR"))
                            CREDIT_HOUR = Objects.requireNonNull(dataSnapshot.child("CREDIT_HOUR").getValue()).toString();
                        if (dataSnapshot.hasChild("DEPARTMENT"))
                            DEPARTMENT = Objects.requireNonNull(dataSnapshot.child("DEPARTMENT").getValue()).toString();
                        if (dataSnapshot.hasChild("SEMESTER"))
                            SEMESTER = Objects.requireNonNull(dataSnapshot.child("SEMESTER").getValue()).toString();
                        if (dataSnapshot.child("FACULTY_NAME").exists())
                            FACULTY_NAME = dataSnapshot.child("FACULTY_NAME").getValue(String.class);
                        if (dataSnapshot.child("FACULTY_ID").exists())
                            FACULTY_ID = dataSnapshot.child("FACULTY_ID").getValue(String.class);

                        list.add(new CourseModel(ID,COURSE_NAME,CREDIT_HOUR,DEPARTMENT,SEMESTER,FACULTY_ID,FACULTY_NAME));

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
}