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
import com.smartscheduler_admin.model.RoomsModel;

import java.util.Objects;

public class ViewEditableRoomActivity extends AppCompatActivity {
    private EditText blockNumber,floorNumber,room_number, room_type;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_editable_room);

        blockNumber = findViewById(R.id.blockNumber);
        floorNumber = findViewById(R.id.floorNumber);
        room_number = findViewById(R.id.roomNumber);
        room_type = findViewById(R.id.roomType);

        CardView updateData = findViewById(R.id.updateCard);
        CardView deleteData = findViewById(R.id.deleteCard);

        RoomsModel model = (RoomsModel) getIntent().getSerializableExtra("room");

        String RoomNumber = "";
        String RoomType = "";
        String BlockNumber = "";
        String FloorNumber = "";

        if (model.getID() != null && !model.getID().equals(""))
            ID = model.getID();
        if (model.getBLOCK_NUM() != null && !model.getBLOCK_NUM().equals(""))
            BlockNumber = model.getBLOCK_NUM();
        if (model.getFLOOR_NUM() != null && !model.getFLOOR_NUM().equals(""))
            FloorNumber = model.getFLOOR_NUM();
        if (model.getROOM() != null && !model.getROOM().equals(""))
            RoomNumber = model.getROOM();
        if (model.getROOM_TYPE() != null && !model.getROOM_TYPE().equals(""))
            RoomType = model.getROOM_TYPE();

        room_number.setText(RoomNumber);
        room_type.setText(RoomType);
        blockNumber.setText(BlockNumber);
        floorNumber.setText(FloorNumber);

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        updateData.setOnClickListener(v -> {
            String name = room_number.getText().toString().trim();
            String block = blockNumber.getText().toString().trim();
            String floor = floorNumber.getText().toString().trim();

            if (block.equalsIgnoreCase("")) {
                Toast.makeText(this, "Add Block Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (floor.equalsIgnoreCase("")) {
                Toast.makeText(this, "Add Floor Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (name.equalsIgnoreCase("")) {
                Toast.makeText(this, "Add Room Number", Toast.LENGTH_SHORT).show();
                return;
            }

            CanAddThisRoom(name);
        });
        deleteData.setOnClickListener(view -> myRef.child("Rooms").child(ID).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ViewEditableRoomActivity.this, "Room Deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ViewEditableRoomActivity.this, "Unable to delete Room", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void CanAddThisRoom(String room_number) {
        DatabaseReference newRef = myRef.getRef();
        newRef.child("Rooms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot s :
                            snapshot.getChildren()) {
                        if (s.child("NUMBER").exists() && !Objects.equals(s.child("NUMBER").getValue(String.class), "")) {
                            if (!Objects.equals(s.getKey(), ID))
                                if (room_number.equalsIgnoreCase(s.child("NUMBER").getValue(String.class))) {
                                    Toast.makeText(ViewEditableRoomActivity.this, "Rooms Already Exists", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                        }
                    }
                    UpdateRoom();
                } else
                    UpdateRoom();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void UpdateRoom() {
        DatabaseReference newRef = myRef.child("Rooms").child(ID);

        newRef.child("ID").setValue(ID);
        newRef.child("NUMBER").setValue(room_number.getText().toString());
        newRef.child("BLOCK_NUM").setValue(blockNumber.getText().toString());
        newRef.child("FLOOR_NUM").setValue(floorNumber.getText().toString());

        if (!room_type.getText().toString().equals("")) {
            newRef.child("ROOM_TYPE").setValue(room_type.getText().toString());
        }

        Toast.makeText(this, "Rooms Updated", Toast.LENGTH_SHORT).show();
    }
}