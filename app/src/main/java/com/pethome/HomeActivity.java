package com.pethome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    TextView txtWelcome, txtMyRequestsCount;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtWelcome = findViewById(R.id.txtWelcome);
        txtMyRequestsCount = findViewById(R.id.txtMyRequestsCount);
        dbHelper = new DatabaseHelper(this);

        checkUserSession();
        loadUserName();
        loadRequestsCount();
        setupBottomNavigation();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRequestsCount();
    }

    private void loadRequestsCount() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", null);
        if (email != null) {
            Cursor cursor = dbHelper.getRequestsByUser(email);
            int count = 0;
            if (cursor != null) {
                int statusIndex = cursor.getColumnIndex("STATUS");
                if (statusIndex != -1) {
                    while (cursor.moveToNext()) {
                        if ("PENDING".equals(cursor.getString(statusIndex))) {
                            count++;
                        }
                    }
                }
                cursor.close();
            }
            if (txtMyRequestsCount != null) {
                txtMyRequestsCount.setText(count + " pending");
            }
        }
    }

    // ==============================
    // Bottom Navigation
    // ==============================
    private void setupBottomNavigation() {

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;

            } else if (id == R.id.nav_browse) {
                startActivity(new Intent(this, AdoptActivity.class));
                return true;

            } else if (id == R.id.nav_requests) {
                startActivity(new Intent(this, OwnerRequestActivity.class));
                return true;

            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }

            return false;
        });
    }

    // ==============================
    // Click Listeners (Quick Actions)
    // ==============================
    private void setupClickListeners() {

        findViewById(R.id.cardListPet).setOnClickListener(v ->
                startActivity(new Intent(this, ListPetActivity.class)));

        findViewById(R.id.cardAdopt).setOnClickListener(v ->
                startActivity(new Intent(this, AdoptActivity.class)));

        findViewById(R.id.cardMyRequests).setOnClickListener(v ->
                startActivity(new Intent(this, MyRequestsActivity.class)));

        findViewById(R.id.cardShelters).setOnClickListener(v ->
                startActivity(new Intent(this, SheltersActivity.class)));

        findViewById(R.id.cardFavorites).setOnClickListener(v ->
                startActivity(new Intent(this, FavoritesActivity.class)));

        findViewById(R.id.cardHistory).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        // See All Buttons
        findViewById(R.id.btnSeeAllPets).setOnClickListener(v ->
                startActivity(new Intent(this, AdoptActivity.class)));

        if (findViewById(R.id.btnSeeAllRequests) != null) {
            findViewById(R.id.btnSeeAllRequests).setOnClickListener(v ->
                    startActivity(new Intent(this, MyRequestsActivity.class)));
        }

        // Hero CTA Button
        findViewById(R.id.heroCta).setOnClickListener(v ->
                startActivity(new Intent(this, AdoptActivity.class)));

        // Profile Avatar (Top Right)
        findViewById(R.id.btnProfile).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
    }

    // ==============================
    // Check Login Session
    // ==============================
    private void checkUserSession() {

        SharedPreferences prefs =
                getSharedPreferences("UserSession", MODE_PRIVATE);

        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    // ==============================
    // Load Username from SQLite
    // ==============================
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
