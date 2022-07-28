package com.smartscheduler_admin.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.smartscheduler_admin.model.ScheduleModel;
import com.smartscheduler_admin.services.ApiServices;
import com.smartscheduler_admin.services.ClientApi;
import com.smartscheduler_admin.services.Data;
import com.smartscheduler_admin.services.MyResponse;
import com.smartscheduler_admin.services.NotificationSender;
import com.smartscheduler_admin.util.Days;
import com.smartscheduler_admin.util.TimeSlots;

import java.util.ArrayList;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Response;

public class ViewEditableScheduleActivity extends AppCompatActivity {
    private int NotificationSent = 0;

    private final ArrayList<String> TeacherIDs = new ArrayList<>();
    private final ArrayList<String> StudentIDs = new ArrayList<>();
    private final ArrayList<String> TeacherDeviceTokens = new ArrayList<>();
    private final ArrayList<String> StudentDeviceTokens = new ArrayList<>();

    private Spinner DaysSpinner;
    private Spinner SemesterSpinner;
    private Spinner FacultySpinner;
    private Spinner CourseSpinner;
    private Spinner TimeSlotSpinner;
    private Spinner RoomSpinner;
    private Spinner DepartmentSpinner;

    private ArrayAdapter<String> adapterDays;
    private ArrayAdapter<String> adapterSemester;
    private ArrayAdapter<String> adapterFaculty;
    private ArrayAdapter<String> adapterCourse;
    private ArrayAdapter<String> adapterTimeSlot;
    private ArrayAdapter<String> adapterRoom;
    private ArrayAdapter<String> adapterDepartment;

    private ArrayList<String> adapterCourseCreditHour = new ArrayList<>();

    DatabaseReference myRef;
    FirebaseAuth mAuth;

    boolean isFirstTimeDepartment = true;
    boolean isFirstTimeSemester = true;

    ArrayList<String> DaysList = new ArrayList<>();
    ArrayList<String> SemesterList = new ArrayList<>();
    ArrayList<String> FacultyName = new ArrayList<>();
    ArrayList<String> CourseList = new ArrayList<>();
    ArrayList<String> TimeSlotList = new ArrayList<>();
    ArrayList<String> RoomList = new ArrayList<>();
    ArrayList<String> DepartmentList = new ArrayList<>();
    ArrayList<String> TeacherIDsList = new ArrayList<>();

    String day = "";
    String timeslot = "";
    String course = "";
    String faculty = "";
    String room = "";
    String department = "";
    String semester = "";
    String FacultyID = "";
    int count = 0;
    SweetAlertDialog sweetAlertDialog;
    ScheduleModel scheduleModel;
    CardView updateCard,deleteCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_editable_schedule);

        scheduleModel = (ScheduleModel) getIntent().getSerializableExtra("schedule");

        count = Integer.parseInt(scheduleModel.getID());

        sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setConfirmButton("OK", Dialog::dismiss);

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Day List
        DaysList.add(Days.MONDAY.toString());
        DaysList.add(Days.TUESDAY.toString());
        DaysList.add(Days.WEDNESDAY.toString());
        DaysList.add(Days.THURSDAY.toString());
        DaysList.add(Days.FRIDAY.toString());

        //Semester List
        SemesterList.add("1");
        SemesterList.add("2");
        SemesterList.add("3");
        SemesterList.add("4");
        SemesterList.add("5");
        SemesterList.add("6");
        SemesterList.add("7");
        SemesterList.add("8");

        //TimeSlot List
        TimeSlotList.add(TimeSlots.SLOT1.toString());
        TimeSlotList.add(TimeSlots.SLOT2.toString());
        TimeSlotList.add(TimeSlots.SLOT3.toString());
        TimeSlotList.add(TimeSlots.SLOT4.toString());
        TimeSlotList.add(TimeSlots.SLOT5.toString());
        TimeSlotList.add(TimeSlots.SLOT6.toString());

        DaysSpinner = findViewById(R.id.allDaysSpinner);
        CourseSpinner = findViewById(R.id.allCoursesSpinner);
        FacultySpinner = findViewById(R.id.allFacultySpinner);
        RoomSpinner = findViewById(R.id.allRoomSpinner);
        SemesterSpinner = findViewById(R.id.allSemesterSpinner);
        DepartmentSpinner = findViewById(R.id.allDepartmentSpinner);
        TimeSlotSpinner = findViewById(R.id.allSlotsSpinner);
        updateCard = findViewById(R.id.updateCard);
        deleteCard = findViewById(R.id.deleteCard);

        //Days Adapter
        adapterDays = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DaysList);
        adapterDays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DaysSpinner.setAdapter(adapterDays);

        DaysSpinner.setSelection(adapterDays.getPosition(scheduleModel.getDAY()));

        //Course Adapter
        adapterCourse = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CourseList);
        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CourseSpinner.setAdapter(adapterCourse);

        //Faculty Adapter
        adapterFaculty = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FacultyName);
        adapterFaculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FacultySpinner.setAdapter(adapterFaculty);

        //Room Adapter
        adapterRoom = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, RoomList);
        adapterRoom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RoomSpinner.setAdapter(adapterRoom);

        //Semester Adapter
        adapterSemester = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SemesterList);
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SemesterSpinner.setAdapter(adapterSemester);
        SemesterSpinner.setSelection(adapterSemester.getPosition(scheduleModel.getSEMESTER()));

        SemesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isFirstTimeSemester)
                {
                    UpdateSubjects();
                }
                isFirstTimeSemester =false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Department Adapter
        adapterDepartment = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DepartmentList);
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DepartmentSpinner.setAdapter(adapterDepartment);

        //Time Slot Adapter
        adapterTimeSlot = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TimeSlotList);
        adapterTimeSlot.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TimeSlotSpinner.setAdapter(adapterTimeSlot);
        TimeSlotSpinner.setSelection(adapterTimeSlot.getPosition(scheduleModel.getTIMESLOT()));

        myRef.child("Rooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = "";
                    if (areaSnapshot.child("NUMBER").exists())
                        areaName = areaSnapshot.child("NUMBER").getValue(String.class);
                    RoomList.add(areaName);
                }
                RoomSpinner.setAdapter(adapterRoom);
                RoomSpinner.setSelection(adapterRoom.getPosition(scheduleModel.getROOM()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                DepartmentSpinner.setSelection(adapterDepartment.getPosition(scheduleModel.getDEPARTMENT()));

                DepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!isFirstTimeDepartment)
                        {
                            UpdateSubjects();
                        }
                        isFirstTimeDepartment = false ;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRef.child("Faculty").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String fName = "";
                    String ID = areaSnapshot.getKey();
                    if (areaSnapshot.child("NAME").exists())
                        fName = areaSnapshot.child("NAME").getValue(String.class);
                    adapterFaculty.add(fName);
                    TeacherIDsList.add(ID);
                }

                FacultySpinner.setAdapter(adapterFaculty);
                FacultySpinner.setSelection(adapterFaculty.getPosition(scheduleModel.getFACULTY()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateCard.setOnClickListener(v -> {
            NotificationSent=0;
            day = DaysSpinner.getSelectedItem().toString();
            timeslot = TimeSlotSpinner.getSelectedItem().toString();
            course = CourseSpinner.getSelectedItem().toString();
            faculty = FacultySpinner.getSelectedItem().toString();
            room = RoomSpinner.getSelectedItem().toString();
            department = DepartmentSpinner.getSelectedItem().toString();
            semester = SemesterSpinner.getSelectedItem().toString();
            FacultyID = TeacherIDsList.get(FacultySpinner.getSelectedItemPosition());

            if (day.equals("")) {
                Toast.makeText(this, "Please Select a Day", Toast.LENGTH_SHORT).show();
                return;
            }

            if (semester.equals("")) {
                Toast.makeText(this, "Please Select a Semester", Toast.LENGTH_SHORT).show();
                return;
            }

            if (faculty.equals("")) {
                Toast.makeText(this, "Please Select a Faculty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (course.equals("")) {
                Toast.makeText(this, "Please Select a Course", Toast.LENGTH_SHORT).show();
                return;
            }

            if (course.equals("SELECT")) {
                Toast.makeText(this, "Please Select a Course", Toast.LENGTH_SHORT).show();
                return;
            }

            if (timeslot.equals("")) {
                Toast.makeText(this, "Please Select a TimeSlot", Toast.LENGTH_SHORT).show();
                return;
            }

            if (room.equals("")) {
                Toast.makeText(this, "Please Select a Room", Toast.LENGTH_SHORT).show();
                return;
            }

            if (department.equals("")) {
                Toast.makeText(this, "Please Select a Department", Toast.LENGTH_SHORT).show();
                return;
            }

            sweetAlertDialog.setTitle("Saving...");
            sweetAlertDialog.setContentText("Please wait");
            if (!sweetAlertDialog.isShowing())
                sweetAlertDialog.show();

            getScheduleAvailability();
        });

        deleteCard.setOnClickListener(view -> myRef.child("Schedule").child(count + "").removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ViewEditableScheduleActivity.this, "Schedule Deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ViewEditableScheduleActivity.this, "Unable to delete Schedule", Toast.LENGTH_SHORT).show();
            }
        }));

        UpdateSubjects();
    }

    private void UpdateSubjects() {
        CourseList.clear();
        CourseList.add("SELECT");

        adapterCourseCreditHour.clear();

        myRef.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String SelectedDepartment = "";
                String SelectedSemester = "";

                String CourseName = "";
                String Department = "";
                String Semester = "" ;
                String CreditHour = "";

                if (DepartmentSpinner!=null && DepartmentSpinner.getSelectedItem()!=null)
                    SelectedDepartment = DepartmentSpinner.getSelectedItem().toString();
                if (SemesterSpinner!=null && SemesterSpinner.getSelectedItem()!=null)
                    SelectedSemester = SemesterSpinner.getSelectedItem().toString();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    if (areaSnapshot.child("COURSE_NAME").exists())
                        CourseName = areaSnapshot.child("COURSE_NAME").getValue(String.class);
                    if (areaSnapshot.child("DEPARTMENT").exists())
                        Department = areaSnapshot.child("DEPARTMENT").getValue(String.class);
                    if (areaSnapshot.child("SEMESTER").exists())
                        Semester = areaSnapshot.child("SEMESTER").getValue(String.class);
                    if (areaSnapshot.child("CREDIT_HOUR").exists())
                        CreditHour = areaSnapshot.child("CREDIT_HOUR").getValue(String.class);

                    if (Department!=null && Department.equals(SelectedDepartment)
                            && Semester!=null && Semester.equals(SelectedSemester)) {
                        adapterCourse.add(CourseName);
                        adapterCourseCreditHour.add(CreditHour);
                    }
                }

                CourseSpinner.setAdapter(adapterCourse);
                CourseSpinner.setSelection(adapterCourse.getPosition(scheduleModel.getCOURSE()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendNotificationToTeacherAndStudent() {
        sweetAlertDialog.setTitle("Sending Notification to");
        sweetAlertDialog.setContentText(TeacherDeviceTokens.size() + " Faculty and " + StudentDeviceTokens.size() + " Students");
        if (!sweetAlertDialog.isShowing())
            sweetAlertDialog.show();

        ApiServices apiServices = ClientApi.getRetrofit("https://fcm.googleapis.com/").create(ApiServices.class);

        for (String s : TeacherDeviceTokens) {
            Data data = new Data("Timetable alert", "New Updated Time Table Available");
            NotificationSender notificationSender = new NotificationSender(data, s);

            apiServices.sendNotification(notificationSender).enqueue(new retrofit2.Callback<MyResponse>() {
                @Override
                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                    if (response.isSuccessful()) {
                        NotificationSent++;

                        if (NotificationSent == TeacherDeviceTokens.size() && StudentDeviceTokens.size() == 0) {
                            Reset();
                            Toast.makeText(ViewEditableScheduleActivity.this, "No Students exist in this Class", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                }
            });
        }

        for (String s : StudentDeviceTokens) {
            Data data = new Data("Time Table Alert", "New Time Table Available");
            NotificationSender notificationSender = new NotificationSender(data, s);

            apiServices.sendNotification(notificationSender).enqueue(new retrofit2.Callback<MyResponse>() {
                @Override
                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                    if (response.isSuccessful()) {
                        NotificationSent++;
                    }
                    if (NotificationSent >= StudentDeviceTokens.size() + TeacherDeviceTokens.size()) {
                        Reset();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                    Reset();
                }
            });
        }

        if (TeacherDeviceTokens.size()==0 && StudentDeviceTokens.size()==0)
            Reset();
    }

    private void getFacultyID(String facultyID, String semester, String department) {
        sweetAlertDialog.setTitle("Getting Faculty ID");
        sweetAlertDialog.setContentText("To send this Timetable");
        if (!sweetAlertDialog.isShowing())
            sweetAlertDialog.show();

        TeacherDeviceTokens.clear();
        TeacherIDs.clear();
        StudentIDs.clear();
        StudentDeviceTokens.clear();

        myRef.child("AppUsers").child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String ID;
                        String FacultyID;

                        ID = dataSnapshot.getKey();

                        if (dataSnapshot.hasChild("facultyID") &&
                                !Objects.equals(dataSnapshot.child("facultyID").getValue(String.class), "")) {
                            FacultyID = Objects.requireNonNull(dataSnapshot.child("facultyID").getValue()).toString();
                            if (FacultyID.equals(facultyID)) {
                                TeacherIDs.add(ID);
                            }
                        }
                    }
                    getAllStudents(semester, department);
                } else {
                    getAllStudents(semester, department);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAllStudents(String semester, String department) {
        sweetAlertDialog.setTitle("Getting Students IDs");
        sweetAlertDialog.setContentText("To send this Timetable");
        if (!sweetAlertDialog.isShowing())
            sweetAlertDialog.show();

        myRef.child("AppUsers").child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String ID;
                        String Semester = "";
                        String Department = "";
                        String userType = "";

                        ID = dataSnapshot.getKey();

                        if (dataSnapshot.hasChild("usertype") &&
                                Objects.equals(dataSnapshot.child("usertype").getValue(String.class), "Student")) {
                            if (dataSnapshot.hasChild("Semester") &&
                                    Objects.equals(dataSnapshot.child("Semester").getValue(String.class), semester)
                                    && dataSnapshot.hasChild("Department") &&
                                    Objects.equals(dataSnapshot.child("Department").getValue(String.class), department)) {
                                Semester = dataSnapshot.child("Semester").getValue(String.class);
                                Department = dataSnapshot.child("Department").getValue(String.class);
                                StudentIDs.add(ID);
                            }

                        }
                    }
                    getAllTeacherDeviceTokens();
                } else {
                    getAllTeacherDeviceTokens();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAllTeacherDeviceTokens() {
        sweetAlertDialog.setTitle("Getting Faculty Device Token");
        sweetAlertDialog.setContentText("To send Notification");
        if (!sweetAlertDialog.isShowing())
            sweetAlertDialog.show();

        myRef.child("DeviceTokens").child("Teachers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        if (TeacherIDs.contains(s.getKey())) {
                            if (s.child("token").exists() && s.child("token").getValue() != null)
                                TeacherDeviceTokens.add(Objects.requireNonNull(s.child("token").getValue()).toString());
                        }
                    }
                    getAllStudentDeviceTokens();
                } else {
                    getAllStudentDeviceTokens();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAllStudentDeviceTokens() {
        sweetAlertDialog.setTitle("Getting Students Device Tokens");
        sweetAlertDialog.setContentText("To send Notification");
        if (!sweetAlertDialog.isShowing())
            sweetAlertDialog.show();

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
                    SendNotificationToTeacherAndStudent();
                } else {
                    SendNotificationToTeacherAndStudent();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void Reset() {
        if (sweetAlertDialog.isShowing())
            sweetAlertDialog.dismiss();

        Toast.makeText(this, "Schedule Updated", Toast.LENGTH_SHORT).show();
    }

    private void getScheduleAvailability() {
        DatabaseReference newRef = myRef.getRef();
        newRef.child("Schedule").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isTimeAvailable = true;
                boolean isFacultyAvailable = true;
                boolean isRoomAvailable = true;
                boolean isClassStudentsAvailable = true;
                int ClassesCountPerWeek = 0;
                int TotalClassesAllowed = Integer.parseInt(adapterCourseCreditHour.get(CourseSpinner.getSelectedItemPosition() - 1));

                DataSnapshot OldTimeTable = null;
                if (snapshot.exists()) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        String Day = s.child("DAY").getValue(String.class);
                        String Timeslot = s.child("TIMESLOT").getValue(String.class);
                        String Course = s.child("COURSE").getValue(String.class);
                        String Room = s.child("ROOM").getValue(String.class);
                        String Department = s.child("DEPARTMENT").getValue(String.class);
                        String Semester = s.child("SEMESTER").getValue(String.class);
                        String oldFacultyID = s.child("FACULTY_ID").getValue(String.class);

                        if (semester.equalsIgnoreCase(Semester) && department.equalsIgnoreCase(Department)
                                && course.equalsIgnoreCase(Course) && !s.getKey().equals(count + "")) {
                            ClassesCountPerWeek++;
                            OldTimeTable = s;
                        }

                        if (day.equalsIgnoreCase(Day) && timeslot.equalsIgnoreCase(Timeslot)
                                && semester.equalsIgnoreCase(Semester)
                                && department.equalsIgnoreCase(Department) && !s.getKey().equals(count + "")) {
                            isClassStudentsAvailable = false;
                            OldTimeTable = s;
                        }

                        if (day.equalsIgnoreCase(Day) && timeslot.equalsIgnoreCase(Timeslot)
                                && FacultyID.equalsIgnoreCase(oldFacultyID) && !s.getKey().equals(count + "")) {
                            isFacultyAvailable = false;
                            OldTimeTable = s;
                        }

                        if (day.equalsIgnoreCase(Day) && timeslot.equalsIgnoreCase(Timeslot)
                                && room.equalsIgnoreCase(Room) && !s.getKey().equals(count + "")) {
                            isRoomAvailable = false;
                            OldTimeTable = s;
                        }

                        if (day.equalsIgnoreCase(Day) && timeslot.equalsIgnoreCase(Timeslot) && course.equalsIgnoreCase(Course)
                                && room.equalsIgnoreCase(Room) && department.equalsIgnoreCase(Department) && semester.equalsIgnoreCase(Semester)
                                && FacultyID.equalsIgnoreCase(oldFacultyID) && !s.getKey().equals(count + "")) {
                            isTimeAvailable = false;
                            OldTimeTable = s;
                        }
                    }

                    if (ClassesCountPerWeek >= TotalClassesAllowed)
                    {
                        String Course = OldTimeTable.child("COURSE").getValue(String.class);

                        sweetAlertDialog.setTitle("Not Allowed");
                        sweetAlertDialog.setContentText("Maximum Class allowed for " + Course + " is " + TotalClassesAllowed + " but already found " + ClassesCountPerWeek + " Classes in the Timetable");
                    }
                    else if (!isClassStudentsAvailable)
                    {
                        String Day = OldTimeTable.child("DAY").getValue(String.class);
                        String Timeslot = OldTimeTable.child("TIMESLOT").getValue(String.class);
                        String Course = OldTimeTable.child("COURSE").getValue(String.class);
                        String Room = OldTimeTable.child("ROOM").getValue(String.class);
                        String Department = OldTimeTable.child("DEPARTMENT").getValue(String.class);
                        String Semester = OldTimeTable.child("SEMESTER").getValue(String.class);
                        String oldFaculty = OldTimeTable.child("FACULTY").getValue(String.class);

                        sweetAlertDialog.setTitle("This SEMESTER Class is Already Arranged in This Timeslot");
                        sweetAlertDialog.setContentText("Day : ".concat(Day).concat("\n")
                                .concat("Timeslot : ".concat(Timeslot)).concat("\n")
                                .concat("Course : ".concat(Course)).concat("\n")
                                .concat("Room : ".concat(Room)).concat("\n")
                                .concat("Department : ".concat(Department)).concat("\n")
                                .concat("Semester : ".concat(Semester)).concat("\n")
                                .concat("Teacher : ".concat(oldFaculty)));
                    }
                    else if (!isRoomAvailable)
                    {
                        String Day = OldTimeTable.child("DAY").getValue(String.class);
                        String Timeslot = OldTimeTable.child("TIMESLOT").getValue(String.class);
                        String Course = OldTimeTable.child("COURSE").getValue(String.class);
                        String Room = OldTimeTable.child("ROOM").getValue(String.class);
                        String Department = OldTimeTable.child("DEPARTMENT").getValue(String.class);
                        String Semester = OldTimeTable.child("SEMESTER").getValue(String.class);
                        String oldFaculty = OldTimeTable.child("FACULTY").getValue(String.class);

                        sweetAlertDialog.setTitle("ROOM NOT AVAILABLE");
                        sweetAlertDialog.setContentText("Day : ".concat(Day).concat("\n")
                                .concat("Timeslot : ".concat(Timeslot)).concat("\n")
                                .concat("Course : ".concat(Course)).concat("\n")
                                .concat("Room : ".concat(Room)).concat("\n")
                                .concat("Department : ".concat(Department)).concat("\n")
                                .concat("Semester : ".concat(Semester)).concat("\n")
                                .concat("Teacher : ".concat(oldFaculty)));
                    }
                    else if (!isFacultyAvailable)
                    {
                        String Day = OldTimeTable.child("DAY").getValue(String.class);
                        String Timeslot = OldTimeTable.child("TIMESLOT").getValue(String.class);
                        String Course = OldTimeTable.child("COURSE").getValue(String.class);
                        String Room = OldTimeTable.child("ROOM").getValue(String.class);
                        String Department = OldTimeTable.child("DEPARTMENT").getValue(String.class);
                        String Semester = OldTimeTable.child("SEMESTER").getValue(String.class);
                        String oldFaculty = OldTimeTable.child("FACULTY").getValue(String.class);

                        sweetAlertDialog.setTitle("FACULTY NOT AVAILABLE");
                        sweetAlertDialog.setContentText("Day : ".concat(Day).concat("\n")
                                .concat("Timeslot : ".concat(Timeslot)).concat("\n")
                                .concat("Course : ".concat(Course)).concat("\n")
                                .concat("Room : ".concat(Room)).concat("\n")
                                .concat("Department : ".concat(Department)).concat("\n")
                                .concat("Semester : ".concat(Semester)).concat("\n")
                                .concat("Teacher : ".concat(oldFaculty)));
                    }
                    else if (isTimeAvailable) {
                        Save();
                    } else {
                        String Day = OldTimeTable.child("DAY").getValue(String.class);
                        String Timeslot = OldTimeTable.child("TIMESLOT").getValue(String.class);
                        String Course = OldTimeTable.child("COURSE").getValue(String.class);
                        String Room = OldTimeTable.child("ROOM").getValue(String.class);
                        String Department = OldTimeTable.child("DEPARTMENT").getValue(String.class);
                        String Semester = OldTimeTable.child("SEMESTER").getValue(String.class);
                        String oldFaculty = OldTimeTable.child("FACULTY").getValue(String.class);

                        sweetAlertDialog.setTitle("TIMESLOT NOT AVAILABLE");
                        sweetAlertDialog.setContentText("Day : ".concat(Day).concat("\n")
                                .concat("Timeslot : ".concat(Timeslot)).concat("\n")
                                .concat("Course : ".concat(Course)).concat("\n")
                                .concat("Room : ".concat(Room)).concat("\n")
                                .concat("Department : ".concat(Department)).concat("\n")
                                .concat("Semester : ".concat(Semester)).concat("\n")
                                .concat("Teacher : ".concat(oldFaculty)));
                    }
                }
                else
                    Save();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Save() {
        DatabaseReference newRef = myRef.child("Schedule").child((count) + "");
        newRef.child("DAY").setValue(day);
        newRef.child("COURSE").setValue(course);
        newRef.child("FACULTY").setValue(faculty);
        newRef.child("ROOM").setValue(room);
        newRef.child("SEMESTER").setValue(semester);
        newRef.child("DEPARTMENT").setValue(department);
        newRef.child("TIMESLOT").setValue(timeslot);
        newRef.child("FACULTY_ID").setValue(FacultyID);

        getFacultyID(FacultyID, semester, department);
    }
}