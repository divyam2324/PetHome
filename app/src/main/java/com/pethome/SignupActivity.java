package com.pethome;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    Button signupBtn;
    TextView loginText;

    EditText name, email, contact, password, confirmPassword;
    RadioGroup gender;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        contact = findViewById(R.id.signup_number);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_cnf_password);
        signupBtn = findViewById(R.id.signup_btn);
        gender = findViewById(R.id.signup_gender);
        loginText = findViewById(R.id.signup_signin);

        // Go back to Login
        loginText.setOnClickListener(v -> finish());

        signupBtn.setOnClickListener(v -> {

            String nameText = name.getText().toString().trim();
            String contactText = contact.getText().toString().trim();
            String emailText = email.getText().toString().trim();
            String passText = password.getText().toString().trim();
            String confirmText = confirmPassword.getText().toString().trim();

            if (nameText.isEmpty()) {
                name.setError("Name Required");
            }
            else if (contactText.isEmpty()) {
                contact.setError("Contact Required");
            }
            else if (contactText.length() < 10) {
                contact.setError("Enter 10 digit number");
            }
            else if (emailText.isEmpty()) {
                email.setError("Email Required");
            }
            else if (!emailText.matches(emailPattern)) {
                email.setError("Enter valid email");
            }
            else if (passText.isEmpty()) {
                password.setError("Password Required");
            }
            else if (passText.length() < 6) {
                password.setError("Minimum 6 characters required");
            }
            else if (!passText.equals(confirmText)) {
                confirmPassword.setError("Passwords do not match");
            }
            else if (gender.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();

                // Go back to login after signup
                finish();
            }
        });
    }
}
