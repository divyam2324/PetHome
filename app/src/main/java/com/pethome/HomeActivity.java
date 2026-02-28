package com.pethome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    TextView txtWelcome;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtWelcome = findViewById(R.id.txtWelcome);
        dbHelper = new DatabaseHelper(this);

        checkUserSession();
        loadUserName();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

// Set selected item
        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;

            } else if (id == R.id.nav_profile) {

                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }

    // Check if user is logged in
    private void checkUserSession() {

        SharedPreferences prefs =
                getSharedPreferences("UserSession", MODE_PRIVATE);

        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // If not logged in → go to Login page
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        }
    }

    // Load username from SQLite
    private void loadUserName() {

        SharedPreferences prefs =
                getSharedPreferences("UserSession", MODE_PRIVATE);

        String email = prefs.getString("email", null);

        if (email != null) {
            String name = dbHelper.getUserName(email);

            if (name != null) {
                txtWelcome.setText("Welcome, " + name + "!");
            } else {
                txtWelcome.setText("Welcome!");
            }
        }
    }
}