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

import java.util.ArrayList;

public class ManageListingsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<PetModel> petList;
    ManageListingsAdapter adapter;
    DatabaseHelper dbHelper;
    TextView txtNoListings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_listings);

        recyclerView = findViewById(R.id.recyclerManageListings);
        txtNoListings = findViewById(R.id.txtNoListings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        petList = new ArrayList<>();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.fabAddPet).setOnClickListener(v -> 
            startActivity(new Intent(this, ListPetActivity.class)));

        loadMyListings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyListings();
    }

    private void loadMyListings() {
        petList.clear();
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String ownerEmail = prefs.getString("email", null);

        if (ownerEmail == null) return;

        Cursor cursor = dbHelper.getPetsByOwner(ownerEmail);

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
                String email = cursor.getString(cursor.getColumnIndexOrThrow("OWNEREMAIL"));

                petList.add(new PetModel(id, name, species, breed, age, gender, children == 1, otherPets == 1, contactName, contactInfo, image, email));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (petList.isEmpty()) {
            txtNoListings.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtNoListings.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter = new ManageListingsAdapter(petList, new ManageListingsAdapter.OnPetActionListener() {
            @Override
            public void onDelete(PetModel pet) {
                dbHelper.deletePet(pet.getId());
                Toast.makeText(ManageListingsActivity.this, "Pet Deleted", Toast.LENGTH_SHORT).show();
                loadMyListings();
            }

            @Override
            public void onEdit(PetModel pet) {
                // Future implementation
                Toast.makeText(ManageListingsActivity.this, "Edit Feature Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
