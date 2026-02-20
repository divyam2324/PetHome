package com.pethome;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNav = findViewById(R.id.bottomNav);

        // Default selected item
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            }
            else if (id == R.id.nav_search) {
                Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            else if (id == R.id.nav_add) {
                Toast.makeText(this, "Add Pet Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            else if (id == R.id.nav_favorites) {
                Toast.makeText(this, "Favorites Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            else if (id == R.id.nav_profile) {
                Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });
    }
}