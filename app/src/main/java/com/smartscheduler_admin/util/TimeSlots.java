package com.smartscheduler_admin.util;

import androidx.annotation.NonNull;

public enum TimeSlots {
        SLOT1("9am-10am"),
        SLOT2("10am-11am"),
        SLOT3("11am-12pm"),
        SLOT4("12pm-01pm"),
        SLOT5("01pm-02pm"),
        SLOT6("02pm-03pm");

        String TimeSlot;

        TimeSlots(String timeSlot) {
                TimeSlot = timeSlot;
        }

        @NonNull
        @Override
        public String toString() {
                return TimeSlot;
        }
}
