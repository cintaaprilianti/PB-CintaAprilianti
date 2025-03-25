package com.example.pb;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class EditProfileActivity extends AppCompatActivity {

    EditText editNama, editEmail, editNIM;
    Button btnSave;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance("https://mobile-870fc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        editNama = findViewById(R.id.editNama);
        editEmail = findViewById(R.id.editEmail);
        editNIM = findViewById(R.id.editNIM);
        btnSave = findViewById(R.id.btnSave);

        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        UserDetails user = snapshot.getValue(UserDetails.class);
                        editNama.setText(user.getNama());
                        editEmail.setText(user.getEmail());
                        editNIM.setText(user.getNim());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(EditProfileActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnSave.setOnClickListener(v -> {
            String newNama = editNama.getText().toString().trim();
            String newEmail = editEmail.getText().toString().trim();
            String newNIM = editNIM.getText().toString().trim();

            if (TextUtils.isEmpty(newNama) || TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newNIM)) {
                Toast.makeText(EditProfileActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentUser != null) {
                String userId = currentUser.getUid();
                databaseReference.child(userId).child("nama").setValue(newNama);
                databaseReference.child(userId).child("email").setValue(newEmail);
                databaseReference.child(userId).child("nim").setValue(newNIM)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}
