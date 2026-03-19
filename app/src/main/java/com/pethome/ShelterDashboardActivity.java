package com.pethome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShelterDashboardActivity extends AppCompatActivity {

    TextView txtRequestCount, txtShelterName;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_dashboard);

        dbHelper = new DatabaseHelper(this);
        txtRequestCount = findViewById(R.id.txtRequestCount);
        txtShelterName = findViewById(R.id.txtShelterName);

        loadDashboardData();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", null);

        if (email != null) {
            String name = dbHelper.getUserName(email);
            if (name != null) {
                txtShelterName.setText(name);
            }

            Cursor cursor = dbHelper.getRequestsForOwner(email);
            int pendingCount = 0;
            if (cursor != null) {
                int statusIndex = cursor.getColumnIndex("STATUS");
                if (statusIndex != -1) {
                    while (cursor.moveToNext()) {
                        if ("PENDING".equals(cursor.getString(statusIndex))) {
                            pendingCount++;
                        }
                    }
                }
                cursor.close();
            }
            txtRequestCount.setText(pendingCount + " New Requests");
        }
    }

    private void setupClickListeners() {
        findViewById(R.id.cardAddPet).setOnClickListener(v -> 
            startActivity(new Intent(this, ListPetActivity.class)));

        findViewById(R.id.cardManageRequests).setOnClickListener(v -> 
            startActivity(new Intent(this, OwnerRequestActivity.class)));

        findViewById(R.id.cardManageListings).setOnClickListener(v -> 
            startActivity(new Intent(this, ManageListingsActivity.class)));

        findViewById(R.id.cardAdoptionHistory).setOnClickListener(v -> 
            startActivity(new Intent(this, HistoryActivity.class)));

        findViewById(R.id.cardMedicalInfo).setOnClickListener(v -> 
            startActivity(new Intent(this, MedicalFacilitiesActivity.class)));

        findViewById(R.id.cardLogout).setOnClickListener(v -> logout());
        
        findViewById(R.id.btnShelterProfile).setOnClickListener(v -> 
            startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void logout() {
        getSharedPreferences("UserSession", MODE_PRIVATE).edit().clear().apply();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
