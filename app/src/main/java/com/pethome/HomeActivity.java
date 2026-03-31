package com.pethome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    TextView txtWelcome, txtMyRequestsCount;
    DatabaseHelper dbHelper;
    RecyclerView recyclerNearbyPets, recyclerHomeDoctors;
    PetAdapter petAdapter;
    DoctorAdapter doctorAdapter;
    ArrayList<PetModel> petList;
    ArrayList<DoctorModel> doctorList;
    FloatingActionButton fabPetCare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtWelcome = findViewById(R.id.txtWelcome);
        txtMyRequestsCount = findViewById(R.id.txtMyRequestsCount);
        recyclerNearbyPets = findViewById(R.id.recyclerNearbyPets);
        recyclerHomeDoctors = findViewById(R.id.recyclerHomeDoctors);
        fabPetCare = findViewById(R.id.fabPetCare);
        
        dbHelper = new DatabaseHelper(this);
        petList = new ArrayList<>();
        doctorList = new ArrayList<>();

        checkUserSession();
        loadUserName();
        loadRequestsCount();
        setupRecyclerViews();
        setupBottomNavigation();
        setupClickListeners();
        
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRequestsCount();
        loadData();
    }

    private void setupRecyclerViews() {
        // Pets RecyclerView
        recyclerNearbyPets.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        petAdapter = new PetAdapter(petList, pet -> {
            Intent intent = new Intent(HomeActivity.this, PetDetailActivity.class);
            intent.putExtra("PETID", pet.getId());
            intent.putExtra("PETNAME", pet.getName());
            intent.putExtra("BREED", pet.getBreed());
            intent.putExtra("AGE", pet.getAge());
            intent.putExtra("GENDER", pet.getGender());
            intent.putExtra("IMAGE", pet.getImage());
            intent.putExtra("OWNEREMAIL", pet.getOwnerEmail());
            startActivity(intent);
        });
        recyclerNearbyPets.setAdapter(petAdapter);

        // Doctors RecyclerView
        recyclerHomeDoctors.setLayoutManager(new LinearLayoutManager(this));
        doctorAdapter = new DoctorAdapter(doctorList, false, null);
        recyclerHomeDoctors.setAdapter(doctorAdapter);
    }

    private void loadData() {
        loadNearbyPets();
        loadAllDoctors();
    }

    private void loadNearbyPets() {
        petList.clear();
        Cursor cursor = dbHelper.getAllAvailablePets();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("PETID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("PETNAME"));
                String species = cursor.getString(cursor.getColumnIndexOrThrow("SPECIES"));
                String breed = cursor.getString(cursor.getColumnIndexOrThrow("BREED"));
                String age = cursor.getString(cursor.getColumnIndexOrThrow("AGE"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("GENDER"));
                int children = cursor.getInt(cursor.getColumnIndexOrThrow("GOODWITHCHILDREN"));
                int otherPets = cursor.getInt(cursor.getColumnIndexOrThrow("GOODWITHOTHERPETS"));
                String contactName = cursor.getString(cursor.getColumnIndexOrThrow("CONTACTNAME"));
                String contactInfo = cursor.getString(cursor.getColumnIndexOrThrow("CONTACTINFO"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("IMAGE"));
                String ownerEmail = cursor.getString(cursor.getColumnIndexOrThrow("OWNEREMAIL"));

                petList.add(new PetModel(id, name, species, breed, age, gender, children == 1, otherPets == 1, contactName, contactInfo, image, ownerEmail));
            } while (cursor.moveToNext());
            cursor.close();
        }
        petAdapter.notifyDataSetChanged();
    }

    private void loadAllDoctors() {
        doctorList.clear();
        Cursor cursor = dbHelper.getAllDoctorsWithShelter();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                String spec = cursor.getString(cursor.getColumnIndexOrThrow("SPECIALIZATION"));
                String contact = cursor.getString(cursor.getColumnIndexOrThrow("CONTACT"));
                String exp = cursor.getString(cursor.getColumnIndexOrThrow("EXPERIENCE"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("SHELTER_EMAIL"));
                String shelterName = cursor.getString(cursor.getColumnIndexOrThrow("SHELTER_NAME"));

                doctorList.add(new DoctorModel(id, name, spec, contact, exp, email, shelterName));
            } while (cursor.moveToNext());
            cursor.close();
        }
        doctorAdapter.notifyDataSetChanged();
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
                startActivity(new Intent(this, MyRequestsActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupClickListeners() {
        findViewById(R.id.cardListPet).setOnClickListener(v ->
                startActivity(new Intent(this, ListPetActivity.class)));

        findViewById(R.id.cardAdopt).setOnClickListener(v ->
                startActivity(new Intent(this, AdoptActivity.class)));

        findViewById(R.id.cardMyRequests).setOnClickListener(v ->
                startActivity(new Intent(this, MyRequestsActivity.class)));

        findViewById(R.id.cardShelters).setOnClickListener(v ->
                startActivity(new Intent(this, SheltersMapActivity.class)));

        findViewById(R.id.cardFavorites).setOnClickListener(v ->
                startActivity(new Intent(this, FavoritesActivity.class)));

        findViewById(R.id.cardHistory).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        findViewById(R.id.btnSeeAllPets).setOnClickListener(v ->
                startActivity(new Intent(this, AdoptActivity.class)));

        findViewById(R.id.heroCta).setOnClickListener(v ->
                startActivity(new Intent(this, AdoptActivity.class)));

        findViewById(R.id.btnProfile).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
        
        fabPetCare.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ChatActivity.class));
        });
    }

    private void checkUserSession() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        if (!isLoggedIn) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void loadUserName() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
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
