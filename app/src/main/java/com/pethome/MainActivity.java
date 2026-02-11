package com.pethome;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.login_btn);
        signupText = findViewById(R.id.main_signup);
        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);

        // Go to Signup page
        signupText.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Login button
        loginBtn.setOnClickListener(v -> {

            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            if (emailText.isEmpty()) {
                email.setError("Email Required");
            }
            else if (!emailText.matches(emailPattern)) {
                email.setError("Enter valid email");
            }
            else if (passwordText.isEmpty()) {
                password.setError("Password Required");
            }
            else if (passwordText.length() < 6) {
                password.setError("Minimum 6 characters required");
            }
            else {
                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, DasboardActivity.class);
                startActivity(intent);

                // Optional: prevent going back to login
                finish();
            }
        });
    }
}
