package com.pethome;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddDoctorActivity extends AppCompatActivity {

    EditText etName, etSpec, etExp, etContact;
    Button btnSave;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        etName = findViewById(R.id.etDoctorName);
        etSpec = findViewById(R.id.etSpecialization);
        etExp = findViewById(R.id.etExperience);
        etContact = findViewById(R.id.etDoctorContact);
        btnSave = findViewById(R.id.btnAddDoctor);

        dbHelper = new DatabaseHelper(this);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String spec = etSpec.getText().toString().trim();
            String exp = etExp.getText().toString().trim();
            String contact = etContact.getText().toString().trim();

            if (name.isEmpty() || spec.isEmpty() || exp.isEmpty() || contact.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
            String shelterEmail = prefs.getString("email", null);

            boolean success = dbHelper.insertDoctor(name, spec, contact, exp, shelterEmail);
            if (success) {
                Toast.makeText(this, "Doctor Added Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add doctor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
