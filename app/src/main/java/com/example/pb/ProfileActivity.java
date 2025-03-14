package com.example.pb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class ProfileActivity extends AppCompatActivity {

    TextView txtNama, txtEmail, txtNIM;
    Button btnLogout;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance("https://mobile-870fc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        txtNama = findViewById(R.id.txtNama);
        txtEmail = findViewById(R.id.txtEmail);
        txtNIM = findViewById(R.id.txtNIM);
        btnLogout = findViewById(R.id.btnLogout);

        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        Log.d("FirebaseDebug", "UID saat ini: " + userId);
                    }

                    if (snapshot.exists()) {
                        UserDetails user = snapshot.getValue(UserDetails.class);
                        txtNama.setText(user.getNama());
                        txtEmail.setText(user.getEmail());
                        txtNIM.setText(user.getNim());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    txtNama.setText("Error Loading Data");
                }
            });
        }

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

    }
}
