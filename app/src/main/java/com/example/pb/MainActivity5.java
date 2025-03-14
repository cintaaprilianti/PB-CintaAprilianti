package com.example.pb;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity5 extends AppCompatActivity {

    TextInputEditText NamaPengguna, EmailPengguna, KataSandi, NimPengguna;
    Button btSignUp;
    FirebaseAuth mAuth;
    private static final String TAG = "MainActivity5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        NamaPengguna = findViewById(R.id.namaPengguna);
        EmailPengguna = findViewById(R.id.email);
        KataSandi = findViewById(R.id.kataSandi);
        NimPengguna = findViewById(R.id.nim);
        btSignUp = findViewById(R.id.btnSignUp);
        CheckBox showPassword = findViewById(R.id.showPassword);

        mAuth = FirebaseAuth.getInstance();

        btSignUp.setOnClickListener(v -> {
            String nama = NamaPengguna.getText().toString().trim();
            String email = EmailPengguna.getText().toString().trim();
            String password = KataSandi.getText().toString().trim();
            String nim = NimPengguna.getText().toString().trim();

            if (TextUtils.isEmpty(nama)) {
                NamaPengguna.setError("Masukkan Nama Pengguna");
                NamaPengguna.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(email)) {
                EmailPengguna.setError("Masukkan Email");
                EmailPengguna.requestFocus();
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                EmailPengguna.setError("Format Email Tidak Valid");
                EmailPengguna.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(password) || password.length() < 6) {
                KataSandi.setError("Password minimal 6 karakter");
                KataSandi.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(nim)) {
                NimPengguna.setError("Masukkan NIM");
                NimPengguna.requestFocus();
                return;
            }

            registerUser(nama, email, password, nim);
        });

        showPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                KataSandi.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                KataSandi.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
    }

    private void registerUser(String nama, String email, String password, String nim) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();

                    DatabaseReference reference = FirebaseDatabase.getInstance("https://mobile-870fc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
                    UserDetails userDetails = new UserDetails(nama, email, nim);
                    reference.child(uid).setValue(userDetails).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Log.d(TAG, "Data pengguna berhasil disimpan di Firebase.");
                            user.sendEmailVerification();
                            Toast.makeText(MainActivity5.this, "Akun berhasil dibuat. Verifikasi email Anda.", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(MainActivity5.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Registrasi Gagal: ", task.getException());
                Toast.makeText(MainActivity5.this, "Registrasi gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        btSignUp.setOnClickListener(View -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
