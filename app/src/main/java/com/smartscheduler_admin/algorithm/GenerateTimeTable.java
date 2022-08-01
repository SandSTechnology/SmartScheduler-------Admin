package com.smartscheduler_admin.algorithm;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smartscheduler_admin.model.CourseModel;
import com.smartscheduler_admin.model.ScheduleModel;
import com.smartscheduler_admin.util.Days;
import com.smartscheduler_admin.util.TimeSlots;

import java.util.ArrayList;
import java.util.Locale;

public class GenerateTimeTable {
    Context context;
    DatabaseReference databaseReference;

    //Data List
    ArrayList<ScheduleModel> SchedulesList = new ArrayList<>();
    ArrayList<String> DaysList = new ArrayList<>();
    ArrayList<String> SemesterList = new ArrayList<>();
    //ArrayAdapter<String> adapterFaculty;
    ArrayList<String> FacultyName = new ArrayList<>();
    ArrayList<CourseModel> CourseList = new ArrayList<>();
    ArrayList<String> adapterCourseCreditHour = new ArrayList<>();
    ArrayList<String> TimeSlotList = new ArrayList<>();
    ArrayList<String> RoomList = new ArrayList<>();
    ArrayList<String> DepartmentList = new ArrayList<>();
    ArrayList<String> TeacherIDsList = new ArrayList<>();

    public GenerateTimeTable(Context c) {
        context = c;
        databaseReference = FirebaseDatabase.getInstance().getReference();

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

        databaseReference.child("Rooms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = "";
                    if (areaSnapshot.child("NUMBER").exists())
                        areaName = areaSnapshot.child("NUMBER").getValue(String.class);
                    RoomList.add(areaName);
                }
                //RoomSpinner.setAdapter(adapterRoom);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Departments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String depName = "";
                    if (areaSnapshot.child("NAME").exists())
                        depName = areaSnapshot.child("NAME").getValue(String.class);
                    DepartmentList.add(depName);
                }
                //DepartmentSpinner.setAdapter(adapterDepartment);

                /*DepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        UpdateSubjects();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Faculty").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String fName = "";
                    String ID = areaSnapshot.getKey();
                    if (areaSnapshot.child("NAME").exists())
                        fName = areaSnapshot.child("NAME").getValue(String.class);
                    FacultyName.add(fName);
                    TeacherIDsList.add(ID);
                }

                //FacultySpinner.setAdapter(adapterFaculty);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String SelectedDepartment = "";
                //String SelectedSemester = "";

                String ID;
                String CourseName = "";
                String Department = "";
                String Semester = "";
                String CreditHour = "";
                String FACULTY_NAME = "";
                String FACULTY_ID = "";

                //if (DepartmentSpinner!=null && DepartmentSpinner.getSelectedItem()!=null)
                //    SelectedDepartment = DepartmentSpinner.getSelectedItem().toString();
                //if (SemesterSpinner!=null && SemesterSpinner.getSelectedItem()!=null)
                //   SelectedSemester = SemesterSpinner.getSelectedItem().toString();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    ID = areaSnapshot.getKey();
                    if (areaSnapshot.child("COURSE_NAME").exists())
                        CourseName = areaSnapshot.child("COURSE_NAME").getValue(String.class);
                    if (areaSnapshot.child("DEPARTMENT").exists())
                        Department = areaSnapshot.child("DEPARTMENT").getValue(String.class);
                    if (areaSnapshot.child("SEMESTER").exists())
                        Semester = areaSnapshot.child("SEMESTER").getValue(String.class);
                    if (areaSnapshot.child("CREDIT_HOUR").exists())
                        CreditHour = areaSnapshot.child("CREDIT_HOUR").getValue(String.class);
                    if (areaSnapshot.child("FACULTY_NAME").exists())
                        FACULTY_NAME = areaSnapshot.child("FACULTY_NAME").getValue(String.class);
                    if (areaSnapshot.child("FACULTY_ID").exists())
                        FACULTY_ID = areaSnapshot.child("FACULTY_ID").getValue(String.class);
                    //if (Department!=null && Department.equals(SelectedDepartment)
                    //        && Semester!=null && Semester.equals(SelectedSemester)) {
                    CourseList.add(new CourseModel(ID, CourseName, CreditHour, Department, Semester,FACULTY_ID,FACULTY_NAME));
                    //adapterCourseCreditHour.add(CreditHour);
                    //}
                }

                //CourseSpinner.setAdapter(adapterCourse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void LetsGenerateTimetable() {
        int ID = 1;
        for (int day = 0; day < DaysList.size(); day++) // From Monday to Friday
        {
            for (int department = 0; department < DepartmentList.size(); department++) // For All Departments
            {
                for (int semester = 0; semester < SemesterList.size(); semester++) // For All Semesters
                {
                    String CurrentDay = DaysList.get(day).toUpperCase(Locale.ROOT);
                    String CurrentDepartment = DepartmentList.get(department).toUpperCase(Locale.ROOT);
                    String CurrentSemester = SemesterList.get(semester).toUpperCase(Locale.ROOT);

                    ArrayList<CourseModel> AllCoursesForSelectedSemester = new ArrayList<>();
                    for (int i = 0; i < CourseList.size(); i++) {
                        String Department = CourseList.get(i).getDEPARTMENT();
                        String Semester = CourseList.get(i).getSEMESTER();

                        if (Department.equalsIgnoreCase(DepartmentList.get(department))
                                && Semester.equalsIgnoreCase(SemesterList.get(semester))) {
                            AllCoursesForSelectedSemester.add(CourseList.get(i));
                        }
                    }

                    for (int i = 0; i < AllCoursesForSelectedSemester.size(); i++) // Add All Courses Classes
                    {
                        int TotalClassesAllowed = Integer.parseInt(AllCoursesForSelectedSemester.get(i).getCREDIT_HOUR());

                        for (int ii = 0; ii < SchedulesList.size(); ii++) {
                            if (SchedulesList.get(ii).getCOURSE().equalsIgnoreCase(AllCoursesForSelectedSemester.get(i).getCOURSE_NAME())
                                    && SchedulesList.get(ii).getDEPARTMENT().equalsIgnoreCase(AllCoursesForSelectedSemester.get(i).getDEPARTMENT())
                                    && SchedulesList.get(ii).getSEMESTER().equalsIgnoreCase(AllCoursesForSelectedSemester.get(i).getSEMESTER())) {
                                --TotalClassesAllowed;
                            }
                        }

                        while (TotalClassesAllowed > 0) {

                            if (SchedulesList.size() == 0) {
                                if (!AllCoursesForSelectedSemester.isEmpty()) {
                                    Save(ID + "", CurrentDay, TimeSlotList.get(0), AllCoursesForSelectedSemester.get(0).getCOURSE_NAME(),
                                            FacultyName.get(0), RoomList.get(0), CurrentSemester, AllCoursesForSelectedSemester.get(0).getCREDIT_HOUR(),
                                            CurrentDepartment);
                                    //Create Time Schedule and Add to TimeScheduleList
                                    Log.e("Timetable", "Day " + day + " Department " + department + " Semester " + semester);
                                }
                                ID++;
                                --TotalClassesAllowed;
                                break;
                            } else //Save After getting ScheduleAvailability
                            {
                                String CurrentTimeSlot = TimeSlotList.get(0);
                                String CurrentRoom = RoomList.get(0);

                                boolean SaveStatusFailed = false ;

                                for (int AddedSchedules = 0; AddedSchedules < SchedulesList.size(); AddedSchedules++) {
                                    boolean isTimeAvailable = true;
                                    boolean isFacultyAvailable = true;
                                    boolean isRoomAvailable = true;
                                    boolean isClassStudentsAvailable = true;
                                    int ClassesCountPerWeek = 0;

                                    String AlreadyAddedDay = SchedulesList.get(AddedSchedules).getDAY();
                                    String AlreadyAddedTimeslot = SchedulesList.get(AddedSchedules).getTIMESLOT();
                                    String AlreadyAddedCourse = SchedulesList.get(AddedSchedules).getCOURSE();
                                    String AlreadyAddedRoom = SchedulesList.get(AddedSchedules).getROOM();
                                    String AlreadyAddedDepartment = SchedulesList.get(AddedSchedules).getDEPARTMENT();
                                    String AlreadyAddedSemester = SchedulesList.get(AddedSchedules).getSEMESTER();
                                    String AlreadyAddedOldFacultyID = SchedulesList.get(AddedSchedules).getFACULTY();

                                    if (CurrentSemester.equalsIgnoreCase(AlreadyAddedSemester) && CurrentDepartment.equalsIgnoreCase(AlreadyAddedDepartment)
                                            && AllCoursesForSelectedSemester.get(i).getCOURSE_NAME().equalsIgnoreCase(AlreadyAddedCourse)) {
                                        ClassesCountPerWeek++;
                                        //OldTimeTable = s;
                                    }

                                    if (CurrentDay.equalsIgnoreCase(AlreadyAddedDay) && CurrentTimeSlot.equalsIgnoreCase(AlreadyAddedTimeslot)
                                            && CurrentSemester.equalsIgnoreCase(AlreadyAddedSemester) && CurrentDepartment.equalsIgnoreCase(AlreadyAddedDepartment)) {
                                        isClassStudentsAvailable = false;
                                        while (!isClassStudentsAvailable)
                                        {
                                            if (TimeSlotList.indexOf(CurrentTimeSlot) <= 5)
                                                CurrentTimeSlot = TimeSlotList.get(TimeSlotList.indexOf(CurrentTimeSlot) + 1);
                                            else
                                                break;

                                            if (CurrentDay.equalsIgnoreCase(AlreadyAddedDay) && CurrentTimeSlot.equalsIgnoreCase(AlreadyAddedTimeslot)
                                                    && CurrentSemester.equalsIgnoreCase(AlreadyAddedSemester) && CurrentDepartment.equalsIgnoreCase(AlreadyAddedDepartment))
                                            {

                                            }
                                            else
                                            {
                                                isClassStudentsAvailable = true ;
                                            }
                                        }
                                        //OldTimeTable = s;
                                    }

                                    if (!isClassStudentsAvailable) {
                                        SaveStatusFailed = true;
                                        break;
                                    }

                                    if (CurrentDay.equalsIgnoreCase(AlreadyAddedDay) && CurrentTimeSlot.equalsIgnoreCase(AlreadyAddedTimeslot)
                                            && AllCoursesForSelectedSemester.get(i).getFACULTY_ID().equalsIgnoreCase(AlreadyAddedOldFacultyID)
                                    && CurrentSemester.equalsIgnoreCase(AlreadyAddedSemester) && CurrentDepartment.equalsIgnoreCase(AlreadyAddedDepartment)) {
                                        isFacultyAvailable = false;
                                        while (!isFacultyAvailable) {
                                            if (TimeSlotList.indexOf(CurrentTimeSlot) <= 5)
                                                CurrentTimeSlot = TimeSlotList.get(TimeSlotList.indexOf(CurrentTimeSlot) + 1);
                                            else
                                                break;

                                            if (CurrentDay.equalsIgnoreCase(AlreadyAddedDay) && CurrentTimeSlot.equalsIgnoreCase(AlreadyAddedTimeslot)
                                                    && AllCoursesForSelectedSemester.get(i).getFACULTY_ID().equalsIgnoreCase(AlreadyAddedOldFacultyID)
                                                    && CurrentSemester.equalsIgnoreCase(AlreadyAddedSemester) && CurrentDepartment.equalsIgnoreCase(AlreadyAddedDepartment)) {

                                            } else {
                                                isFacultyAvailable = true;
                                            }
                                        }
                                        //OldTimeTable = s;
                                    }

                                    if (!isFacultyAvailable ) {
                                        SaveStatusFailed = true;
                                        break;
                                    }

                                    if (CurrentDay.equalsIgnoreCase(AlreadyAddedDay) && CurrentTimeSlot.equalsIgnoreCase(AlreadyAddedTimeslot)
                                            && CurrentRoom.equalsIgnoreCase(AlreadyAddedRoom)) {
                                        isRoomAvailable = false;
                                        while (!isRoomAvailable) {
                                            if (RoomList.indexOf(CurrentRoom) < RoomList.size())
                                                CurrentRoom = RoomList.get(RoomList.indexOf(RoomList) + 1);
                                            else
                                                break;

                                            if (CurrentDay.equalsIgnoreCase(AlreadyAddedDay) && CurrentTimeSlot.equalsIgnoreCase(AlreadyAddedTimeslot)
                                                    && CurrentRoom.equalsIgnoreCase(AlreadyAddedRoom)) {
                                            } else {
                                                isRoomAvailable = true;
                                            }
                                        }
                                        //OldTimeTable = s;
                                    }

                                    if(!isRoomAvailable ) {
                                        SaveStatusFailed = true;
                                        break;
                                    }

                                    /*if (CurrentDay.equalsIgnoreCase(AlreadyAddedDay) && CurrentTimeSlot.equalsIgnoreCase(AlreadyAddedTimeslot) && AllCoursesForSelectedSemester.get(i).getCOURSE_NAME().equalsIgnoreCase(AlreadyAddedCourse)
                                            && CurrentRoom.equalsIgnoreCase(AlreadyAddedRoom) && CurrentDepartment.equalsIgnoreCase(AlreadyAddedDepartment) && CurrentSemester.equalsIgnoreCase(AlreadyAddedSemester)
                                            && AllCoursesForSelectedSemester.get(i).getFACULTY_ID().equalsIgnoreCase(AlreadyAddedOldFacultyID)) {
                                        isTimeAvailable = false;
                                        //OldTimeTable = s;
                                    }*/

                                }

                                if (!SaveStatusFailed) {
                                    Save(ID + "", CurrentDay, CurrentTimeSlot, AllCoursesForSelectedSemester.get(i).getCOURSE_NAME(),
                                            AllCoursesForSelectedSemester.get(i).getFACULTY_ID(), CurrentRoom, CurrentSemester, AllCoursesForSelectedSemester.get(i).getCREDIT_HOUR(),
                                            CurrentDepartment);

                                    //Create Time Schedule and Add to TimeScheduleList
                                    Log.e("Timetable", "Day " + day + " Department " + department + " Semester " + semester);
                                }

                                --TotalClassesAllowed;
                                break;
                            }
                        }

                    }

                }
            }
        }
    }

    void Save(String ID, String DAY, String timeSLOT, String COURSE, String FACULTY, String ROOM, String SEMESTER, String CREDIT_HOUR, String DEPARTMENT) {
        SchedulesList.add(new ScheduleModel(ID, DAY, timeSLOT, COURSE, FACULTY, ROOM, SEMESTER, CREDIT_HOUR, DEPARTMENT,"",""));

        Log.i("Timetable","ID " + ID + " DAY " + DAY + " Timeslot " + timeSLOT + " COURSE " + COURSE
                + " FACULTY " + FACULTY + " ROOM " + ROOM + " SEMESTER " +
                SEMESTER + " CREDIT_HOUR " + CREDIT_HOUR + " DEPARTMENT " + DEPARTMENT);
    }
}