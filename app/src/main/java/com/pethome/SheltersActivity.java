package com.pethome;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SheltersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ShelterModel> shelterList;
    ShelterAdapter adapter;
    DatabaseHelper dbHelper;
    TextView txtNoShelters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelters);

        recyclerView = findViewById(R.id.recyclerShelters);
        txtNoShelters = findViewById(R.id.txtNoShelters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        shelterList = new ArrayList<>();

        loadShelters();
    }

    private void loadShelters() {
        shelterList.clear();
        Cursor cursor = dbHelper.getAllShelters();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"));
                String contact = cursor.getString(cursor.getColumnIndexOrThrow("CONTACT"));
                String city = cursor.getString(cursor.getColumnIndexOrThrow("CITY"));
                String medicalInfo = cursor.getString(cursor.getColumnIndexOrThrow("MEDICAL_INFO"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("LATITUDE"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("LONGITUDE"));

                shelterList.add(new ShelterModel(name, email, contact, city, medicalInfo, latitude, longitude));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (shelterList.isEmpty()) {
            txtNoShelters.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtNoShelters.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter = new ShelterAdapter(shelterList);
        recyclerView.setAdapter(adapter);
    }
}
