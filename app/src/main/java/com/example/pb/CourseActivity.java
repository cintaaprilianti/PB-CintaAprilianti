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

public class CourseActivity extends AppCompatActivity {

    EditText editCourse;
    Button btnAddCourse;
    RecyclerView recyclerViewCourses;
    ArrayList<String> courseList;
    CourseAdapter adapter;

    private static final String PREFS_NAME = "CoursePrefs";
    private static final String COURSES_KEY = "courses";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        editCourse = findViewById(R.id.editCourse);
        btnAddCourse = findViewById(R.id.btnAddCourse);
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);

        courseList = loadData();

        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(courseList, new CourseAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                courseList.remove(position);
                adapter.notifyItemRemoved(position);
                saveData();
                Toast.makeText(CourseActivity.this, "Course deleted", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewCourses.setAdapter(adapter);

        btnAddCourse.setOnClickListener(v -> {
            String course = editCourse.getText().toString().trim();
            if (!course.isEmpty()) {
                courseList.add(course);
                adapter.notifyDataSetChanged();
                editCourse.setText("");
                saveData();
                Toast.makeText(CourseActivity.this, "Course saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CourseActivity.this, "Course cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> set = new HashSet<>(courseList);
        editor.putStringSet(COURSES_KEY, set);
        editor.apply();
    }

    private ArrayList<String> loadData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> set = prefs.getStringSet(COURSES_KEY, new HashSet<>());

        return new ArrayList<>(set);
    }
}