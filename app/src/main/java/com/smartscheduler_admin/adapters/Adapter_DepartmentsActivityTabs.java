package com.smartscheduler_admin.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.smartscheduler_admin.fragments.course.AddNewCourseFragment;
import com.smartscheduler_admin.fragments.course.ViewAllCoursesFragment;
import com.smartscheduler_admin.fragments.department.AddNewDepartmentFragment;
import com.smartscheduler_admin.fragments.department.ViewAllDepartmentsFragment;

public class Adapter_DepartmentsActivityTabs extends FragmentStateAdapter {
    int NumOfTabs;

    public Adapter_DepartmentsActivityTabs(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int numOfTabs) {
        super(fragmentManager, lifecycle);
        NumOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ViewAllDepartmentsFragment();
            case 1:
                return new AddNewDepartmentFragment();
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return NumOfTabs;
    }
}
