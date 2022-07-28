package com.smartscheduler_admin.fragments.room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.util.Objects;

public class AddNewRoomFragment extends Fragment {
    private EditText room_number, room_type;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    int count = 0;
    boolean isCountingDone = false;

    public AddNewRoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_room, container, false);

        room_number = view.findViewById(R.id.roomNumber);
        room_type = view.findViewById(R.id.roomType);
        CardView submitData = view.findViewById(R.id.submitCard);

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        submitData.setOnClickListener(v -> {
            String name = room_number.getText().toString().trim();

            if (name.equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "Add Room Number", Toast.LENGTH_SHORT).show();
                return;
            }

            CanAddThisRoom(name);
        });

        getScheduleNumber();
        return view;
    }

    private void CanAddThisRoom(String room_number) {
        DatabaseReference newRef = myRef.getRef();
        newRef.child("Rooms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot s :
                            snapshot.getChildren()) {
                        if (s.child("NUMBER").exists() && !Objects.equals(s.child("NUMBER").getValue(String.class), ""))
                        {
                            if (room_number .equalsIgnoreCase(s.child("NUMBER").getValue(String.class)))
                            {
                                Toast.makeText(getContext(), "Rooms Already Exists", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    SaveRoom();
                }
                else
                    SaveRoom();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SaveRoom() {
        DatabaseReference newRef = myRef.child("Rooms").child((++count) + "");

        newRef.child("ID").setValue(count);
        newRef.child("NUMBER").setValue(room_number.getText().toString());
        if (!room_type.getText().toString().equals(""))
        {
            newRef.child("ROOM_TYPE").setValue(room_type.getText().toString());
        }

        Toast.makeText(getContext(), "Rooms Added", Toast.LENGTH_SHORT).show();

        room_number.setText("");
        room_type.setText("");
    }

    private void getScheduleNumber() {
        DatabaseReference newRef = myRef.getRef();
        newRef.child("Rooms").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
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