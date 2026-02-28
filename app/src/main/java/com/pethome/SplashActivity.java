package com.pethome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2500; // 2.5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    v.setPadding(
                            insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                            insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                            insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                            insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
                    );
                    return insets;
                });

        ImageView imageView = findViewById(R.id.splash_image);

        // Load GIF from raw folder (Best Practice)
        Glide.with(this)
                .asGif()
                .load(R.raw.splash_gif)
                .into(imageView);

        // Navigate after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_DELAY);
    }
}