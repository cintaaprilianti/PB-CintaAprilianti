package com.example.pb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class StudyPlanActivity extends AppCompatActivity {

    EditText editStudyPlan;
    Button btnAddStudyPlan;
    RecyclerView recyclerViewStudyPlans;
    ArrayList<String> studyPlanList;
    StudyPlanAdapter adapter;

    private static final String PREFS_NAME = "StudyPlanPrefs";
    private static final String STUDY_PLANS_KEY = "studyPlans";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_plan);

        editStudyPlan = findViewById(R.id.editStudyPlan);
        btnAddStudyPlan = findViewById(R.id.btnAddStudyPlan);
        recyclerViewStudyPlans = findViewById(R.id.recyclerViewStudyPlans);

        studyPlanList = loadData();

        recyclerViewStudyPlans.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudyPlanAdapter(studyPlanList, new StudyPlanAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                studyPlanList.remove(position);
                adapter.notifyItemRemoved(position);
                saveData();
                Toast.makeText(StudyPlanActivity.this, "Study plan deleted", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewStudyPlans.setAdapter(adapter);

        btnAddStudyPlan.setOnClickListener(v -> {
            String studyPlan = editStudyPlan.getText().toString().trim();
            if (!studyPlan.isEmpty()) {
                studyPlanList.add(studyPlan);
                adapter.notifyDataSetChanged();
                editStudyPlan.setText("");
                saveData();
                Toast.makeText(StudyPlanActivity.this, "Study plan saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StudyPlanActivity.this, "Study plan cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> set = new HashSet<>(studyPlanList);
        editor.putStringSet(STUDY_PLANS_KEY, set);
        editor.apply();
    }

    private ArrayList<String> loadData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> set = prefs.getStringSet(STUDY_PLANS_KEY, new HashSet<>());

        return new ArrayList<>(set);
    }
}
