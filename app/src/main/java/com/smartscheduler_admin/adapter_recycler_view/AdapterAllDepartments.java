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
import com.smartscheduler_admin.activities.ViewEditableDepartmentActivity;
import com.smartscheduler_admin.model.DepartmentsModel;

import java.util.ArrayList;

public class AdapterAllDepartments extends RecyclerView.Adapter<AdapterAllDepartments.MyHolder> {
    ArrayList<DepartmentsModel> al;
    Context context;
    Fragment fragment;

    public AdapterAllDepartments(ArrayList<DepartmentsModel> al, Context c, Fragment f) {
        this.al = al;
        this.context = c;
        fragment = f;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.recycler_item_department, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        final DepartmentsModel model = al.get(position);

        if (model.getDEPARTMENT()!=null && !model.getDEPARTMENT().equals(""))
            holder.department.setText("" .concat("Department: " + model.getDEPARTMENT()));

        holder.view.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ViewEditableDepartmentActivity.class).putExtra("department",model));
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView department;
        View view;

        public MyHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.card_view);

            department = itemView.findViewById(R.id.departmentName);

        }
    }
}
