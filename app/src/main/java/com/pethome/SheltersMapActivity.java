package com.pethome;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class SheltersMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseHelper dbHelper;
    private ArrayList<ShelterModel> shelterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelters_map);

        dbHelper = new DatabaseHelper(this);
        shelterList = new ArrayList<>();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        loadShelters();
    }

    private void loadShelters() {
        Cursor cursor = dbHelper.getAllShelters();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"));
                String contact = cursor.getString(cursor.getColumnIndexOrThrow("CONTACT"));
                String city = cursor.getString(cursor.getColumnIndexOrThrow("CITY"));
                String medInfo = cursor.getString(cursor.getColumnIndexOrThrow("MEDICAL_INFO"));
                double lat = cursor.getDouble(cursor.getColumnIndexOrThrow("LATITUDE"));
                double lng = cursor.getDouble(cursor.getColumnIndexOrThrow("LONGITUDE"));

                // Only add if location is set (not 0.0)
                if (lat != 0.0 || lng != 0.0) {
                    shelterList.add(new ShelterModel(name, email, contact, city, medInfo, lat, lng));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng defaultFocus = new LatLng(20.5937, 78.9629); // India center as fallback

        if (shelterList.isEmpty()) {
             mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultFocus, 5f));
             return;
        }

        for (ShelterModel shelter : shelterList) {
            LatLng loc = new LatLng(shelter.getLatitude(), shelter.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(loc)
                    .title(shelter.getName())
                    .snippet(shelter.getCity() + " | " + shelter.getContact()));
        }

        // Focus on the first shelter
        LatLng first = new LatLng(shelterList.get(0).getLatitude(), shelterList.get(0).getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(first, 10f));
    }
}
