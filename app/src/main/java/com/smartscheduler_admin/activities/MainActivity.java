package com.smartscheduler_admin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smartscheduler_admin.R;
import com.smartscheduler_admin.algorithm.GenerateTimeTable;
import com.smartscheduler_admin.model.SendNotificationScheduleModel;
import com.smartscheduler_admin.services.ApiServices;
import com.smartscheduler_admin.services.ClientApi;
import com.smartscheduler_admin.services.Data;
import com.smartscheduler_admin.services.MyResponse;
import com.smartscheduler_admin.services.NotificationSender;
import com.smartscheduler_admin.util.BaseUtil;
import com.smartscheduler_admin.util.TimeSlots;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    DatabaseReference myRef;
    SimpleDateFormat simpleDateFormat;
    String CurrentTime;
    Calendar calender;
    int timeDifference;
    ArrayList<SendNotificationScheduleModel> NotificationSentList = new ArrayList<>();
    private Handler mHandler;
    private final ArrayList<String> StudentIDs = new ArrayList<>();
    private final ArrayList<String> StudentDeviceTokens = new ArrayList<>();
    GenerateTimeTable generateTimeTable = new GenerateTimeTable(this);
    ImageView imageView;

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                SendClassNotification();
            }
            catch (Exception e)
            {
                Log.i("Error",e.getMessage());
            }finally {
                int mInterval = 60000;
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();

        myRef = FirebaseDatabase.getInstance().getReference();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String token = new BaseUtil(this).getDeviceToken();
            if (token != null && !token.equals(""))
                myRef.child("DeviceTokens").child("Teachers").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("token")
                        .setValue(token);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.headerImg);
        imageView.setOnClickListener(view -> generateTimeTable.LetsGenerateTimetable());

        CardView facultiesCard = findViewById(R.id.facultiesCard);
        CardView coursesCard = findViewById(R.id.coursesCard);
        //CardView programsCard = findViewById(R.id.programsCard);
        CardView departmentsCard = findViewById(R.id.departmentsCard);
        CardView roomsCard = findViewById(R.id.roomsCard);
        CardView schedulesCard = findViewById(R.id.schedulesCard);
        CardView logoutCard = findViewById(R.id.logoutCard);

        facultiesCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewAllFacultiesActivity.class);
            startActivity(intent);
        });

        coursesCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CoursesActivity.class);
            startActivity(intent);
        });

        departmentsCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DepartmentsActivity.class);
            startActivity(intent);
        });

        roomsCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RoomsActivity.class);
            startActivity(intent);
        });

        schedulesCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SchedulesActivity.class);
            startActivity(intent);
        });

        logoutCard.setOnClickListener(v -> Logout());

        getAllSchedule();


        //generateTimeTable.LetsGenerateTimetable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_button) {
            Logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        new BaseUtil(this).ClearPreferences();
        startActivity(new Intent(MainActivity.this, LoginActivity.class)); //Go back to login page
        finish();
    }

    private void Notification(String tid,String startTime,String Department, String Semester) {
        myRef.child("DeviceTokens").child("Teachers").child(tid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("token").exists() && !Objects.requireNonNull(snapshot.child("token").getValue()).toString().equals("")) {
                        String tok = snapshot.child("token").getValue(String.class);
                        sendNotification(tok, "Be ready", "Your Class in about to start at " + startTime);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getAllStudents(Semester, Department,startTime);
    }

    private void getAllStudents(String semester, String department,String startTime) {
        StudentIDs.clear();
        myRef.child("AppUsers").child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String ID;

                        ID = dataSnapshot.getKey();

                        if (dataSnapshot.hasChild("usertype") &&
                                Objects.equals(dataSnapshot.child("usertype").getValue(String.class), "Student")) {
                            if (dataSnapshot.hasChild("Semester") &&
                                    Objects.equals(dataSnapshot.child("Semester").getValue(String.class), semester)
                                    && dataSnapshot.hasChild("Department") &&
                                    Objects.equals(dataSnapshot.child("Department").getValue(String.class), department)) {
                                StudentIDs.add(ID);
                            }

                        }
                    }
                    if (!StudentIDs.isEmpty())
                        myRef.child("DeviceTokens").child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot s : snapshot.getChildren()) {
                                    if (StudentIDs.contains(s.getKey())) {
                                        if (s.child("token").exists() && s.child("token").getValue() != null)
                                            StudentDeviceTokens.add(Objects.requireNonNull(s.child("token").getValue()).toString());
                                    }
                                }
                                sendNotificationToStudents("Be ready", "Your Class in about to start at " + startTime);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendNotification(String deviceToken, String title, String message) {
        ApiServices apiServices = ClientApi.getRetrofit("https://fcm.googleapis.com/").create(ApiServices.class);

        Data data = new Data(title, message);
        NotificationSender notificationSender = new NotificationSender(data, deviceToken);

        apiServices.sendNotification(notificationSender).enqueue(new retrofit2.Callback<MyResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void sendNotificationToStudents(String title, String message) {
        ApiServices apiServices = ClientApi.getRetrofit("https://fcm.googleapis.com/").create(ApiServices.class);

        Data data;
        NotificationSender notificationSender ;

        if (!StudentDeviceTokens.isEmpty())
            for (String s : StudentDeviceTokens) {
            data = new Data(title, message);
            notificationSender = new NotificationSender(data, s);

            apiServices.sendNotification(notificationSender).enqueue(new retrofit2.Callback<MyResponse>() {
                @Override
                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {

                }

                @Override
                public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {

                }
            });
        }
    }

    private void getAllSchedule() {
        myRef.child("Schedule").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NotificationSentList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String ID;
                        String DAY= "";
                        String TIMESLOT= "";
                        String COURSE= "";
                        String FACULTY= "";
                        String ROOM= "";
                        String SEMESTER= "";
                        String CREDIT_HOUR= "";
                        String DEPARTMENT = "";
                        String Faculty_ID = "";

                        ID = dataSnapshot.getKey();
                        if (dataSnapshot.hasChild("DAY"))
                            DAY = Objects.requireNonNull(dataSnapshot.child("DAY").getValue()).toString();
                        if (dataSnapshot.hasChild("TIMESLOT"))
                            TIMESLOT = dataSnapshot.child("TIMESLOT").getValue(String.class);
                        if (dataSnapshot.hasChild("COURSE"))
                            COURSE = dataSnapshot.child("COURSE").getValue(String.class);
                        if (dataSnapshot.hasChild("FACULTY"))
                            FACULTY = dataSnapshot.child("FACULTY").getValue(String.class);
                        if (dataSnapshot.hasChild("ROOM"))
                            ROOM = dataSnapshot.child("ROOM").getValue(String.class);
                        if (dataSnapshot.hasChild("SEMESTER"))
                            SEMESTER = dataSnapshot.child("SEMESTER").getValue(String.class);
                        if (dataSnapshot.hasChild("CREDIT_HOUR"))
                            CREDIT_HOUR = dataSnapshot.child("CREDIT_HOUR").getValue(String.class);
                        if (dataSnapshot.hasChild("DEPARTMENT"))
                            DEPARTMENT = dataSnapshot.child("DEPARTMENT").getValue(String.class);
                        if (dataSnapshot.hasChild("FACULTY_ID"))
                            Faculty_ID = dataSnapshot.child("FACULTY_ID").getValue(String.class);

                        NotificationSentList.add(new SendNotificationScheduleModel(ID,DAY,TIMESLOT,COURSE,FACULTY,ROOM,SEMESTER,
                                CREDIT_HOUR,DEPARTMENT,false,Faculty_ID));

                    }
                    if (!NotificationSentList.isEmpty()) {
                        StartTimerToSendNotification();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void StartTimerToSendNotification() {
        startRepeatingTask();
    }

    private void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    protected void onDestroy() {
        stopRepeatingTask();
        super.onDestroy();
    }

    private void SendClassNotification() {
        calender = Calendar.getInstance();
        //calender.setTime(new Date("Tue Jul 26 12:56:00 GMT+05:00 2022"));

        SimpleDateFormat justHour = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String CurrentDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calender.getTime());

        simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        CurrentTime = simpleDateFormat.format(calender.getTime());

        if (!NotificationSentList.isEmpty())
        {
            for (SendNotificationScheduleModel model: NotificationSentList)
            {
                if (model.getNotificationSent())
                    continue;
                if (!model.getDAY().equalsIgnoreCase(CurrentDay))
                    continue;

                String TID = model.getFaculty_ID();

                String timeslot = model.getTIMESLOT();
                String department = model.getDEPARTMENT();
                String semester = model.getSEMESTER();

                String TimeSlotTaken = "09:00 am";
                if (timeslot.equalsIgnoreCase(TimeSlots.SLOT1.toString()))
                {
                    TimeSlotTaken = "09:00 am";
                }else if (timeslot.equalsIgnoreCase(TimeSlots.SLOT2.toString()))
                {
                    TimeSlotTaken = "10:00 am";
                }
                else if (timeslot.equalsIgnoreCase(TimeSlots.SLOT3.toString()))
                {
                    TimeSlotTaken = "11:00 am";
                }
                else if (timeslot.equalsIgnoreCase(TimeSlots.SLOT4.toString()))
                {
                    TimeSlotTaken = "12:00 pm";
                }
                else if (timeslot.equalsIgnoreCase(TimeSlots.SLOT5.toString()))
                {
                    TimeSlotTaken = "01:00 pm";
                }
                else if (timeslot.equalsIgnoreCase(TimeSlots.SLOT6.toString()))
                {
                    TimeSlotTaken = "02:00 pm";
                }

                try {
                    Date date1 = justHour.parse(TimeSlotTaken);
                    Date date2 = simpleDateFormat.parse(CurrentTime);

                    long difference;
                    if (Objects.requireNonNull(date1).getTime() > Objects.requireNonNull(date2).getTime())
                        difference = Objects.requireNonNull(date1).getTime() - Objects.requireNonNull(date2).getTime();
                    else
                        continue;

                    int days = (int) (difference / (1000 * 60 * 60 * 24));
                    int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                    int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);

                    if (hours > 0)
                    {
                        timeDifference = ( hours * 60 ) + min ;
                        min = ( hours * 60 ) + min;
                    }
                    else
                        timeDifference = min;
                    Log.e("Time Difference","in Minutes is " + min);
                    if (timeDifference > 0 && timeDifference <= 5) {
                        getTeacherUID(TID,TimeSlotTaken,department,semester);
                        model.setNotificationSent(true);
                    }
                } catch (ParseException e) {
                    Log.e("ERRORRRRRRRR",e.getMessage());
                }
            }
        }
    }

    private void getTeacherUID(String faculty_id,String StartTime,String Department, String Semester) {
        myRef.child("AppUsers").child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot teacher : snapshot.getChildren())
                    {
                        if (teacher.child("facultyID").exists())
                        {
                            String FacultyID = teacher.child("facultyID").getValue(String.class);
                            if (Objects.equals(FacultyID, faculty_id))
                            {
                                String UID = teacher.getKey();
                                Notification(UID,StartTime,Department,Semester);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}