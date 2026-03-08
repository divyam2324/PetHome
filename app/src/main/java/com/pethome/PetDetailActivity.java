package com.pethome;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PetDetailActivity extends AppCompatActivity {

    TextView txtName, txtBreed, txtAge, txtGender;
    ImageView imgPet;
    Button btnAdopt;

    DatabaseHelper dbHelper;

    String name, breed, age, gender, image;
    int petId;
    String ownerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        dbHelper = new DatabaseHelper(this);

        txtName = findViewById(R.id.detailName);
        txtBreed = findViewById(R.id.detailBreed);
        txtAge = findViewById(R.id.detailAge);
        txtGender = findViewById(R.id.detailGender);
        imgPet = findViewById(R.id.detailImage);
        btnAdopt = findViewById(R.id.btnAdopt);

        // RECEIVE DATA FROM INTENT
        petId = getIntent().getIntExtra("PETID", -1);
        name = getIntent().getStringExtra("PETNAME");
        breed = getIntent().getStringExtra("BREED");
        age = getIntent().getStringExtra("AGE");
        gender = getIntent().getStringExtra("GENDER");
        image = getIntent().getStringExtra("IMAGE");
        ownerEmail = getIntent().getStringExtra("OWNEREMAIL");

        txtName.setText(name);
        txtBreed.setText("Breed: " + breed);
        txtAge.setText("Age: " + age);
        txtGender.setText("Gender: " + gender);

        if (image != null && !image.isEmpty()) {
            // Use same Glide logic as PetAdapter to handle both Drawables and Uris
            int resId = getResources().getIdentifier(image, "drawable", getPackageName());
            if (resId != 0) {
                Glide.with(this).load(resId).placeholder(R.drawable.ic_add).into(imgPet);
            } else {
                Glide.with(this).load(Uri.parse(image)).placeholder(R.drawable.ic_add).into(imgPet);
            }
        } else {
            imgPet.setImageResource(R.drawable.ic_add);
        }

        btnAdopt.setOnClickListener(v -> confirmAdoption());
    }

    private void confirmAdoption() {
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
