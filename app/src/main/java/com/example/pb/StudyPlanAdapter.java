package com.example.pb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class StudyPlanAdapter extends RecyclerView.Adapter<StudyPlanAdapter.ViewHolder> {
    private ArrayList<String> studyPlans;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public StudyPlanAdapter(ArrayList<String> studyPlans, OnDeleteClickListener deleteClickListener) {
        this.studyPlans = studyPlans;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_study_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.studyPlanText.setText(studyPlans.get(position));
        holder.btnDelete.setOnClickListener(v -> deleteClickListener.onDeleteClick(position));
    }

    @Override
    public int getItemCount() {
        return studyPlans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studyPlanText;
        ImageButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            studyPlanText = itemView.findViewById(R.id.tvStudyPlanName);
            btnDelete = itemView.findViewById(R.id.btnDeleteStudyPlan);
        }
    }
}
