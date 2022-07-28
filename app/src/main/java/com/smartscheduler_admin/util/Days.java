package com.smartscheduler_admin.util;

import androidx.annotation.NonNull;

public enum Days {
        MONDAY("MONDAY"),
        TUESDAY("TUESDAY"),
        WEDNESDAY("WEDNESDAY"),
        THURSDAY("THURSDAY"),
        FRIDAY("FRIDAY"),
        SATURDAY("SATURDAY"),
        SUNDAY("SUNDAY");

        String Day;

        Days(String day) {
                Day = day;
        }

        @NonNull
        @Override
        public String toString() {
                return Day;
        }
}
