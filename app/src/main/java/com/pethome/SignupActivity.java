package com.pethome;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class SignupActivity extends AppCompatActivity {

    Button signupBtn;
    TextView loginText;

    EditText name, email, contact, password, confirmPassword, sCity;
    RadioGroup gender;
    String sGender;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Spinner spinner;
    String[] role = {"Choose a role", "User", "NGO / Shelter"};
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = openOrCreateDatabase("Pethome.db", MODE_PRIVATE, null);

        String tablequery = "CREATE TABLE IF NOT EXISTS USERS(" +
                "USERID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME VARCHAR(100)," +
                "EMAIL VARCHAR(50)," +
                "CONTACT VARCHAR(15)," +
                "PASSWORD VARCHAR(25)," +
                "GENDER VARCHAR(10)," +
                "CITY VARCHAR(20)," +
                "ROLE VARCHAR(20))";
        db.execSQL(tablequery);

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        contact = findViewById(R.id.signup_number);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_cnf_password);
        gender = findViewById(R.id.signup_gender);
        sCity = findViewById(R.id.signup_city);
        loginText = findViewById(R.id.signup_signin);
        signupBtn = findViewById(R.id.signup_btn);
        spinner = findViewById(R.id.signup_role);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                SignupActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                role);
        spinner.setAdapter(adapter);

        gender.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton radioButton = findViewById(i);
            sGender = radioButton.getText().toString();
        });

        loginText.setOnClickListener(v -> finish());

        signupBtn.setOnClickListener(v -> {

            String nameText = name.getText().toString().trim();
            String contactText = contact.getText().toString().trim();
            String emailText = email.getText().toString().trim();
            String passText = password.getText().toString().trim();
            String confirmText = confirmPassword.getText().toString().trim();
            String cityText = sCity.getText().toString().trim();
            String roleText = spinner.getSelectedItem().toString();

            // VALIDATIONS
            if (nameText.isEmpty()) {
                name.setError("Name Required");
                return;
            }

            if (contactText.isEmpty() || contactText.length() < 10) {
                contact.setError("Enter valid 10 digit number");
                return;
            }

            if (emailText.isEmpty() || !emailText.matches(emailPattern)) {
                email.setError("Enter valid email");
                return;
            }

            if (passText.isEmpty() || passText.length() < 6) {
                password.setError("Minimum 6 characters required");
                return;
            }

            if (!passText.equals(confirmText)) {
                confirmPassword.setError("Passwords do not match");
                return;
            }

            if (gender.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
                return;
            }

            // CHECK IF USER EXISTS (SAFE QUERY)
            Cursor cursor = db.rawQuery(
                    "SELECT * FROM USERS WHERE EMAIL=? OR CONTACT=?",
                    new String[]{emailText, contactText}
            );

            if (cursor.moveToFirst()) {
                Toast.makeText(SignupActivity.this,
                        "User Already Exists",
                        Toast.LENGTH_SHORT).show();
                cursor.close();
                return;
            }

            cursor.close();

            // INSERT USER
            db.execSQL("INSERT INTO USERS (NAME, EMAIL, CONTACT, PASSWORD, GENDER, CITY, ROLE) VALUES (?,?,?,?,?,?,?)",
                    new  Object[]{
                            nameText,
                            emailText,
                            contactText,
                            passText,
                            sGender,
                            cityText,
                            roleText
                    });

            Toast.makeText(SignupActivity.this,
                    "Signup Successfully",
                    Toast.LENGTH_SHORT).show();

            Snackbar.make(v,
                    "Signup Successfully",
                    Snackbar.LENGTH_LONG).show();

            finish();
        });
    }
}