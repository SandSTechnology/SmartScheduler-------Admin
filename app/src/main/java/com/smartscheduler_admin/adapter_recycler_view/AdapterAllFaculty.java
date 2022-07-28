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
import com.smartscheduler_admin.activities.ViewEditableFacultyActivity;
import com.smartscheduler_admin.activities.ViewEditableRoomActivity;
import com.smartscheduler_admin.model.FacultyModel;
import com.smartscheduler_admin.model.FacultyModel;

import java.util.ArrayList;

public class AdapterAllFaculty extends RecyclerView.Adapter<AdapterAllFaculty.MyHolder> {
    ArrayList<FacultyModel> al;
    Context context;

    public AdapterAllFaculty(ArrayList<FacultyModel> al, Context c) {
        this.al = al;
        this.context = c;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.recycler_item_faculty, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        final FacultyModel model = al.get(position);

        if (model.getNAME()!=null && !model.getNAME().equals(""))
            holder.name.setText("" .concat(model.getNAME()));
        if (model.getDEPARTMENT()!=null && !model.getDEPARTMENT().equals(""))
            holder.department.setText("Department : " .concat(model.getDEPARTMENT()));
        if (model.getSUBJECT()!=null && !model.getSUBJECT().equals(""))
            holder.subject.setText("Subject : " .concat(model.getSUBJECT()));
        if (model.getSHORT_NAME()!=null && !model.getSHORT_NAME().equals(""))
            holder.shortName.setText("Short  Name : " .concat(model.getSHORT_NAME()));
        if (model.getEMAIL()!=null && !model.getEMAIL().equals("") && model.getMOBILE_NUMBER()!=null && !model.getMOBILE_NUMBER().equals(""))
            holder.email_mobile.setText("".concat(model.getEMAIL().concat(" | ").concat(model.getMOBILE_NUMBER())));

        holder.view.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ViewEditableFacultyActivity.class).putExtra("faculty",model));
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView name,shortName,email_mobile,semester,subject,department;
        View view;

        public MyHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.card_view);

            name = itemView.findViewById(R.id.teacherName);
            subject = itemView.findViewById(R.id.subjectName);
            department = itemView.findViewById(R.id.departmentName);
            shortName = itemView.findViewById(R.id.teacherShortName);
            email_mobile = itemView.findViewById(R.id.Email_Mobile);
            semester = itemView.findViewById(R.id.Semester);
        }
    }
}