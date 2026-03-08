package com.pethome;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class AdoptActivity extends AppCompatActivity {

    RecyclerView recyclerPets;
    DatabaseHelper dbHelper;
    ArrayList<PetModel> petList;
    PetAdapter adapter;
    TextView txtNoPets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt);

        recyclerPets = findViewById(R.id.recyclerPets);
        txtNoPets = findViewById(R.id.txtNoPets);
        dbHelper = new DatabaseHelper(this);
        petList = new ArrayList<>();

        recyclerPets.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PetAdapter(petList, pet -> {
            Intent intent = new Intent(AdoptActivity.this, PetDetailActivity.class);
            intent.putExtra("PETID", pet.getId());
            intent.putExtra("PETNAME", pet.getName());
            intent.putExtra("BREED", pet.getBreed());
            intent.putExtra("AGE", pet.getAge());
            intent.putExtra("GENDER", pet.getGender());
            intent.putExtra("IMAGE", pet.getImage());
            intent.putExtra("OWNEREMAIL", pet.getOwnerEmail());
            startActivity(intent);
        });

        recyclerPets.setAdapter(adapter);

        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPets();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_browse);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_browse) {
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

    private void loadPets() {
        petList.clear();
        Cursor cursor = dbHelper.getAllAvailablePets();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("PETID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("PETNAME"));
                String breed = cursor.getString(cursor.getColumnIndexOrThrow("BREED"));
                String age = cursor.getString(cursor.getColumnIndexOrThrow("AGE"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("GENDER"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("IMAGE"));
                String ownerEmail = cursor.getString(cursor.getColumnIndexOrThrow("OWNEREMAIL"));

                petList.add(new PetModel(id, name, breed, age, gender, image, ownerEmail));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (petList.isEmpty()) {
            txtNoPets.setVisibility(View.VISIBLE);
            recyclerPets.setVisibility(View.GONE);
        } else {
            txtNoPets.setVisibility(View.GONE);
            recyclerPets.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }
}
