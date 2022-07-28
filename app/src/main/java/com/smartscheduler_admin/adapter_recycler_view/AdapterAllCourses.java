package com.smartscheduler_admin.adapter_recycler_view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.smartscheduler_admin.R;
import com.smartscheduler_admin.activities.ViewEditableCourseActivity;
import com.smartscheduler_admin.model.CourseModel;
import com.smartscheduler_admin.model.FacultyModel;

import java.util.ArrayList;

public class AdapterAllCourses extends RecyclerView.Adapter<AdapterAllCourses.MyHolder> {
    ArrayList<CourseModel> al;
    Context context;
    Fragment fragment;

    public AdapterAllCourses(ArrayList<CourseModel> al, Context c, Fragment f) {
        this.al = al;
        this.context = c;
        fragment = f;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.recycler_item_course, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        final CourseModel model = al.get(position);

        if (model.getCOURSE_NAME()!=null && !model.getCOURSE_NAME().equals(""))
            holder.courseName.setText("Course : " .concat(model.getCOURSE_NAME()));
        if (model.getCREDIT_HOUR()!=null && !model.getCREDIT_HOUR().equals(""))
            holder.credit_hour.setText("Credit Hour : " .concat(model.getCREDIT_HOUR()));
        if (model.getDEPARTMENT()!=null && !model.getDEPARTMENT().equals(""))
            holder.department.setText(" Department : " .concat(model.getDEPARTMENT()));
        if (model.getSEMESTER()!=null && !model.getSEMESTER().equals(""))
            holder.semester.setText(" Semester : " .concat(model.getSEMESTER()));

        holder.view.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ViewEditableCourseActivity.class).putExtra("course",model));
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView courseName,credit_hour,department,semester;
        View view;

        public MyHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.card_view);

            courseName = itemView.findViewById(R.id.courseName);
            credit_hour = itemView.findViewById(R.id.credit_hour);
            department = itemView.findViewById(R.id.department);
            semester = itemView.findViewById(R.id.semester);
        }
    }
}
