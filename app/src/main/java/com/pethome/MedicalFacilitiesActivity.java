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

public class MedicalFacilitiesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DoctorModel> doctorList;
    DoctorAdapter adapter;
    DatabaseHelper dbHelper;
    TextView txtNoDoctors;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_facilities);

        recyclerView = findViewById(R.id.recyclerDoctors);
        txtNoDoctors = findViewById(R.id.txtNoDoctors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        doctorList = new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = prefs.getString("email", null);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.fabAddDoctor).setOnClickListener(v -> 
            startActivity(new Intent(this, AddDoctorActivity.class)));

        loadDoctors();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDoctors();
    }

    private void loadDoctors() {
        doctorList.clear();
        if (userEmail == null) return;

        Cursor cursor = dbHelper.getDoctorsByShelter(userEmail);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                String spec = cursor.getString(cursor.getColumnIndexOrThrow("SPECIALIZATION"));
                String contact = cursor.getString(cursor.getColumnIndexOrThrow("CONTACT"));
                String exp = cursor.getString(cursor.getColumnIndexOrThrow("EXPERIENCE"));

                doctorList.add(new DoctorModel(id, name, spec, contact, exp, userEmail));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (doctorList.isEmpty()) {
            txtNoDoctors.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtNoDoctors.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter = new DoctorAdapter(doctorList, true, doctor -> {
            dbHelper.deleteDoctor(doctor.getId());
            Toast.makeText(this, "Doctor removed", Toast.LENGTH_SHORT).show();
            loadDoctors();
        });
        recyclerView.setAdapter(adapter);
    }
}
