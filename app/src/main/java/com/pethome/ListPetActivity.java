package com.pethome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ListPetActivity extends AppCompatActivity {

    EditText etPetName, etBreed, etAge, etContactName, etContactInfo, etCareInfo;
    Spinner spSpecies;
    RadioGroup rgGender;
    CheckBox cbChildren, cbOtherPets;
    Button btnSubmit, btnSelectImage;
    ImageView imgPet;

    DatabaseHelper databaseHelper;

    Uri imageUri;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pet);

        databaseHelper = new DatabaseHelper(this);

        etPetName = findViewById(R.id.etPetName);
        etBreed = findViewById(R.id.etBreed);
        etAge = findViewById(R.id.etAge);
        etContactName = findViewById(R.id.etContactName);
        etContactInfo = findViewById(R.id.etContactInfo);
        etCareInfo = findViewById(R.id.etCareInfo);
        spSpecies = findViewById(R.id.spSpecies);
        rgGender = findViewById(R.id.rgGender);
        cbChildren = findViewById(R.id.cbChildren);
        cbOtherPets = findViewById(R.id.cbOtherPets);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imgPet = findViewById(R.id.imgPet);

        String[] species = {"Dog", "Cat", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                species
        );
        spSpecies.setAdapter(adapter);

        btnSelectImage.setOnClickListener(v -> openGallery());

        btnSubmit.setOnClickListener(v -> submitPet());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri != null) {
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                try {
                    getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                imgPet.setImageURI(imageUri);
            }
        }
    }

    private void submitPet() {

        String petName = etPetName.getText().toString().trim();
        String species = spSpecies.getSelectedItem().toString();
        String breed = etBreed.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String contactName = etContactName.getText().toString().trim();
        String contactInfo = etContactInfo.getText().toString().trim();
        String careInfo = etCareInfo.getText().toString().trim();

        int selectedGenderId = rgGender.getCheckedRadioButtonId();

        if (petName.isEmpty() || breed.isEmpty() || age.isEmpty()
                || contactName.isEmpty() || contactInfo.isEmpty()
                || selectedGenderId == -1 || imageUri == null) {

            Toast.makeText(this, "Please fill all fields & select image", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedGender = findViewById(selectedGenderId);
        String gender = selectedGender.getText().toString();

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String ownerEmail = prefs.getString("email", "");

        boolean inserted = databaseHelper.insertPet(
                petName,
                species,
                breed,
                age,
                gender,
                cbChildren.isChecked(),
                cbOtherPets.isChecked(),
                contactName,
                contactInfo,
                imageUri.toString(),
                ownerEmail,
                careInfo
        );

        if (inserted) {
            Toast.makeText(this, "Pet Listed Successfully!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to list pet", Toast.LENGTH_SHORT).show();
        }
    }
}
