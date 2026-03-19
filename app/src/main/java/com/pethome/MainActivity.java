package com.pethome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    TextView signupText;
    EditText email, password;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // AUTO LOGIN CHECK
        SharedPreferences prefs =
                getSharedPreferences("UserSession", MODE_PRIVATE);

        if (prefs.getBoolean("isLoggedIn", false)) {
            String role = prefs.getString("role", "User");
            if ("NGO / Shelter".equals(role)) {
                startActivity(new Intent(this, ShelterDashboardActivity.class));
            } else {
                startActivity(new Intent(this, HomeActivity.class));
            }
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.login_btn);
        signupText = findViewById(R.id.main_signup);
        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);

        dbHelper = new DatabaseHelper(this);

        signupText.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        loginBtn.setOnClickListener(v -> {

            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            if (emailText.isEmpty()) {
                email.setError("Email Required");
                return;
            }

            if (!emailText.matches(emailPattern)) {
                email.setError("Enter valid email");
                return;
            }

            if (passwordText.isEmpty()) {
                password.setError("Password Required");
                return;
            }

            if (passwordText.length() < 6) {
                password.setError("Minimum 6 characters required");
                return;
            }

            Cursor cursor = dbHelper.loginUser(emailText, passwordText);

            if (cursor.moveToFirst()) {
                String role = cursor.getString(cursor.getColumnIndexOrThrow("ROLE"));

                getSharedPreferences("UserSession", MODE_PRIVATE)
                        .edit()
                        .putString("email", emailText)
                        .putString("role", role)
                        .putBoolean("isLoggedIn", true)
                        .apply();

                Toast.makeText(MainActivity.this,
                        "Login Successful",
                        Toast.LENGTH_SHORT).show();

                if ("NGO / Shelter".equals(role)) {
                    startActivity(new Intent(MainActivity.this, ShelterDashboardActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
                finish();

            } else {
                Toast.makeText(MainActivity.this,
                        "Invalid Email or Password",
                        Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        });
    }
}
