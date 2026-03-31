package com.pethome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView recyclerFavorites;
    DatabaseHelper dbHelper;
    ArrayList<PetModel> petList;
    PetAdapter adapter;
    TextView txtNoFavorites;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerFavorites = findViewById(R.id.recyclerFavorites);
        txtNoFavorites = findViewById(R.id.txtNoFavorites);
        dbHelper = new DatabaseHelper(this);
        petList = new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = prefs.getString("email", "");

        recyclerFavorites.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new PetAdapter(petList, pet -> {
            Intent intent = new Intent(FavoritesActivity.this, PetDetailActivity.class);
            intent.putExtra("PETID", pet.getId());
            intent.putExtra("PETNAME", pet.getName());
            intent.putExtra("SPECIES", pet.getSpecies());
            intent.putExtra("BREED", pet.getBreed());
            intent.putExtra("AGE", pet.getAge());
            intent.putExtra("GENDER", pet.getGender());
            intent.putExtra("CHILDREN", pet.isGoodWithChildren());
            intent.putExtra("OTHER_PETS", pet.isGoodWithOtherPets());
            intent.putExtra("CONTACT_NAME", pet.getContactName());
            intent.putExtra("CONTACT_INFO", pet.getContactInfo());
            intent.putExtra("IMAGE", pet.getImage());
            intent.putExtra("OWNEREMAIL", pet.getOwnerEmail());
            startActivity(intent);
        });

        recyclerFavorites.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        // Assuming there's a favorites item in the menu. If not, don't set selected.
        // bottomNav.setSelectedItemId(R.id.nav_favorites); 

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_browse) {
                startActivity(new Intent(this, AdoptActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_requests) {
                startActivity(new Intent(this, MyRequestsActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void loadFavorites() {
        petList.clear();
        Cursor cursor = dbHelper.getFavoritePets(userEmail);

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

        if (petList.isEmpty()) {
            txtNoFavorites.setVisibility(View.VISIBLE);
            recyclerFavorites.setVisibility(View.GONE);
        } else {
            txtNoFavorites.setVisibility(View.GONE);
            recyclerFavorites.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }
}
