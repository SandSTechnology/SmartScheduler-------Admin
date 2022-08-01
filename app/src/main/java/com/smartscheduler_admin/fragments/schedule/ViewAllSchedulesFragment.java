package com.smartscheduler_admin.fragments.schedule;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smartscheduler_admin.R;
import com.smartscheduler_admin.adapter_recycler_view.AdapterAllSchedules;
import com.smartscheduler_admin.model.ScheduleModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ViewAllSchedulesFragment extends Fragment {
    ArrayList<ScheduleModel> list;
    AdapterAllSchedules adapter;
    RecyclerView recyclerView;
    View NoRecordFoundView;
    DatabaseReference myRef;
    ProgressBar loadingBar;
    FirebaseUser firebaseUser;
    ValueEventListener allValueListener=null;

    public ViewAllSchedulesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_schedules, container, false);
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
        adapter = new AdapterAllSchedules(list, getActivity(), this);

        recyclerView.setAdapter(adapter);
        return view;
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
        myRef.child("Schedule").addValueEventListener(allValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String ID= "";
                        String DAY= "";
                        String TIMESLOT= "";
                        String COURSE= "";
                        String FACULTY= "";
                        String ROOM= "";
                        String SEMESTER= "";
                        String CREDIT_HOUR= "";
                        String DEPARTMENT = "";
                        String BLOCK_NUM= "";
                        String FLOOR_NUM = "";

                        ID = dataSnapshot.getKey();
                        if (dataSnapshot.hasChild("DAY"))
                            DAY = Objects.requireNonNull(dataSnapshot.child("DAY").getValue()).toString();
                        if (dataSnapshot.hasChild("TIMESLOT"))
                            TIMESLOT = dataSnapshot.child("TIMESLOT").getValue().toString();
                        if (dataSnapshot.hasChild("COURSE"))
                            COURSE = dataSnapshot.child("COURSE").getValue().toString();
                        if (dataSnapshot.hasChild("FACULTY"))
                            FACULTY = dataSnapshot.child("FACULTY").getValue().toString();
                        if (dataSnapshot.hasChild("ROOM"))
                            ROOM = dataSnapshot.child("ROOM").getValue().toString();
                        if (dataSnapshot.hasChild("SEMESTER"))
                            SEMESTER = dataSnapshot.child("SEMESTER").getValue().toString();
                        if (dataSnapshot.hasChild("CREDIT_HOUR"))
                            CREDIT_HOUR = dataSnapshot.child("CREDIT_HOUR").getValue().toString();
                        if (dataSnapshot.hasChild("DEPARTMENT"))
                            DEPARTMENT = dataSnapshot.child("DEPARTMENT").getValue().toString();
                        if (dataSnapshot.hasChild("BLOCK_NUM"))
                            BLOCK_NUM = dataSnapshot.child("BLOCK_NUM").getValue().toString();
                        if (dataSnapshot.hasChild("FLOOR_NUM"))
                            FLOOR_NUM = dataSnapshot.child("FLOOR_NUM").getValue().toString();

                        list.add(new ScheduleModel(ID,DAY,TIMESLOT,COURSE,FACULTY,ROOM,SEMESTER,
                                CREDIT_HOUR,DEPARTMENT,BLOCK_NUM,FLOOR_NUM));

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