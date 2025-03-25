package com.example.pb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class HomeActivity extends AppCompatActivity {

    TextView txtUsername, txtEmail;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    CardView cardProfile, cardAssignment, cardCourse, cardStudyPlan;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        cardProfile = findViewById(R.id.cardProfile);
        cardAssignment = findViewById(R.id.cardAssignment);
        cardCourse = findViewById(R.id.cardCourse);
        cardStudyPlan = findViewById(R.id.cardStudyPlan);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });

        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        UserDetails user = snapshot.getValue(UserDetails.class);
                        txtUsername.setText(user.getNama());
                        txtEmail.setText(user.getEmail());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    txtUsername.setText("Error Loading Data");
                }
            });
        }

        cardProfile.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));
        cardAssignment.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AssignmentActivity.class)));
        cardCourse.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, CourseActivity.class)));
        cardStudyPlan.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, StudyPlanActivity.class)));
    }
}
