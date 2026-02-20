package com.pethome;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    TextView signupText;
    EditText email, password;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.login_btn);
        signupText = findViewById(R.id.main_signup);
        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);
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
        // Go to Signup page
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

            // ✅ SAFE QUERY (NO STRING CONCATENATION)
            Cursor cursor = db.rawQuery(
                    "SELECT * FROM USERS WHERE (EMAIL=? OR CONTACT=?) AND PASSWORD=?",
                    new String[]{emailText, emailText, passwordText}
            );

            if (cursor.moveToFirst()) {
                // User exists → login success
                Toast.makeText(MainActivity.this,
                        "Login Successful",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                // User not found
                Toast.makeText(MainActivity.this,
                        "Invalid Email or Password",
                        Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        });
    }
}
