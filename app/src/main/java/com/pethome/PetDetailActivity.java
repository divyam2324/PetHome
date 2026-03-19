package com.pethome;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PetDetailActivity extends AppCompatActivity {

    TextView txtName, txtBreed, txtAge, txtGender, txtCareInfo, txtOwnerInfo;
    ImageView imgPet;
    Button btnAdopt;

    DatabaseHelper dbHelper;

    String name, breed, age, gender, image, contactName, contactInfo, ownerEmail;
    int petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        dbHelper = new DatabaseHelper(this);

        txtName = findViewById(R.id.detailName);
        txtBreed = findViewById(R.id.detailBreed);
        txtAge = findViewById(R.id.detailAge);
        txtGender = findViewById(R.id.detailGender);
        txtCareInfo = findViewById(R.id.detailCareInfo);
        txtOwnerInfo = findViewById(R.id.detailOwnerInfo);
        imgPet = findViewById(R.id.detailImage);
        btnAdopt = findViewById(R.id.btnAdopt);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // RECEIVE DATA FROM INTENT
        petId = getIntent().getIntExtra("PETID", -1);
        name = getIntent().getStringExtra("PETNAME");
        breed = getIntent().getStringExtra("BREED");
        age = getIntent().getStringExtra("AGE");
        gender = getIntent().getStringExtra("GENDER");
        image = getIntent().getStringExtra("IMAGE");
        ownerEmail = getIntent().getStringExtra("OWNEREMAIL");
        contactName = getIntent().getStringExtra("CONTACT_NAME");
        contactInfo = getIntent().getStringExtra("CONTACT_INFO");

        txtName.setText(name);
        txtBreed.setText(breed);
        txtAge.setText(age);
        txtGender.setText(gender);
        
        if (contactName != null && !contactName.isEmpty()) {
            txtOwnerInfo.setText("Posted by: " + contactName + "\nContact: " + contactInfo);
        } else {
            txtOwnerInfo.setText("Contact: " + ownerEmail);
        }

        loadExtraDetails();

        if (image != null && !image.isEmpty()) {
            int resId = getResources().getIdentifier(image, "drawable", getPackageName());
            if (resId != 0) {
                Glide.with(this).load(resId).placeholder(R.drawable.ic_pets).into(imgPet);
            } else {
                Glide.with(this).load(Uri.parse(image)).placeholder(R.drawable.ic_pets).into(imgPet);
            }
        }

        btnAdopt.setOnClickListener(v -> confirmAdoption());
    }

    private void loadExtraDetails() {
        // Fetch CARE_INFO from DB as it might not be in Intent
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT CARE_INFO FROM PETS WHERE PETID=?", new String[]{String.valueOf(petId)});
        if (cursor != null && cursor.moveToFirst()) {
            String care = cursor.getString(0);
            if (care != null && !care.isEmpty()) {
                txtCareInfo.setText(care);
            }
            cursor.close();
        }
    }

    private void confirmAdoption() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String role = prefs.getString("role", "User");
        
        if ("NGO / Shelter".equals(role)) {
            Toast.makeText(this, "Shelters cannot request adoptions", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Adoption Request")
                .setMessage("Send adoption request for " + name + "?")
                .setPositiveButton("Yes", (dialog, which) -> sendRequest())
                .setNegativeButton("No", null)
                .show();
    }

    private void sendRequest() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = prefs.getString("email", null);

        if (userEmail == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.hasAlreadyRequested(petId, userEmail)) {
            Toast.makeText(this, "You already requested this pet", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.insertRequest(
                petId, name, breed, age, gender, image, userEmail, ownerEmail
        );

        if (success) {
            Toast.makeText(this, "Request Sent Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to send request", Toast.LENGTH_SHORT).show();
        }
    }
}
