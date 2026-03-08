package com.pethome;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SheltersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelters);

        setTitle("Nearby Shelters");
    }
}