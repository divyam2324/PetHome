package com.pethome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Removed EdgeToEdge to simplify and ensure visibility
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.splash_image);

        try {
            // Load GIF from raw folder
            Glide.with(this)
                    .asGif()
                    .load(R.raw.splash_gif)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to a static image if GIF fails
            imageView.setImageResource(R.drawable.logo_1);
        }

        // Navigate after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish();
        }, SPLASH_DELAY);
    }
}
