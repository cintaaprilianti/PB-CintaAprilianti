package com.example.pb;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class AssignmentActivity extends AppCompatActivity {

    EditText editAssignment, editDueDate;
    Button btnAddAssignment;
    RecyclerView recyclerViewAssignments;
    ArrayList<String> assignmentList;
    AssignmentAdapter adapter;

    private static final String PREFS_NAME = "AssignmentPrefs";
    private static final String ASSIGNMENTS_KEY = "assignments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        editAssignment = findViewById(R.id.editAssignment);
        editDueDate = findViewById(R.id.editDueDate);
        btnAddAssignment = findViewById(R.id.btnAddAssignment);
        recyclerViewAssignments = findViewById(R.id.recyclerViewAssignments);

        assignmentList = loadData();

        recyclerViewAssignments.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AssignmentAdapter(assignmentList, new AssignmentAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                assignmentList.remove(position);
                adapter.notifyItemRemoved(position);
                saveData();
                Toast.makeText(AssignmentActivity.this, "Assignment deleted", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewAssignments.setAdapter(adapter);

        editDueDate.setOnClickListener(v -> showDatePicker());

        btnAddAssignment.setOnClickListener(v -> {
            String assignment = editAssignment.getText().toString().trim();
            String dueDate = editDueDate.getText().toString().trim();
            if (!assignment.isEmpty() && !dueDate.isEmpty()) {
                assignmentList.add(assignment + " (Due: " + dueDate + ")");
                adapter.notifyDataSetChanged();
                editAssignment.setText("");
                editDueDate.setText("");
                saveData();
                Toast.makeText(AssignmentActivity.this, "Assignment saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AssignmentActivity.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    editDueDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ASSIGNMENTS_KEY, TextUtils.join(",", assignmentList));
        editor.apply();
    }

    private ArrayList<String> loadData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedData = prefs.getString(ASSIGNMENTS_KEY, "");
        return savedData.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(savedData.split(",")));
    }
}
