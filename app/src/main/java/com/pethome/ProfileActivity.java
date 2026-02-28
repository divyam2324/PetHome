package com.pethome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    TextView txtName, txtEmail, txtPhone, txtGender, txtCity, txtRole;
    Button btnLogout;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtName = findViewById(R.id.profileName);
        txtEmail = findViewById(R.id.profileEmail);
        txtPhone = findViewById(R.id.profilePhone);
        txtGender = findViewById(R.id.profileGender);
        txtCity = findViewById(R.id.profileCity);
        txtRole = findViewById(R.id.profileRole);
        btnLogout = findViewById(R.id.btnLogout);

        dbHelper = new DatabaseHelper(this);

        loadUserData();
        setupBottomNav();
        setupLogout();
    }

    private void loadUserData() {

        SharedPreferences prefs =
                getSharedPreferences("UserSession", MODE_PRIVATE);

        String email = prefs.getString("email", null);

        if (email != null) {

            Cursor cursor = dbHelper.getUserByEmail(email);

            if (cursor != null && cursor.moveToFirst()) {

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("NAME"));

                String userEmail = cursor.getString(
                        cursor.getColumnIndexOrThrow("EMAIL"));

                String phone = cursor.getString(
                        cursor.getColumnIndexOrThrow("CONTACT"));

                String gender = cursor.getString(
                        cursor.getColumnIndexOrThrow("GENDER"));

                String city = cursor.getString(
                        cursor.getColumnIndexOrThrow("CITY"));

                String role = cursor.getString(
                        cursor.getColumnIndexOrThrow("ROLE"));

                txtName.setText(name);
                txtEmail.setText(userEmail);
                txtPhone.setText(phone);
                txtGender.setText(gender);
                txtCity.setText(city);
                txtRole.setText(role);

                cursor.close();
            }
        }
    }

    private void setupLogout() {

        btnLogout.setOnClickListener(v -> {

            getSharedPreferences("UserSession", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupBottomNav() {

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setSelectedItemId(R.id.nav_profile);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                finish();
                return true;
            }

            return id == R.id.nav_profile;
        });
    }
}