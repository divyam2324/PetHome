package com.pethome;

import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class SheltersMapActivity extends AppCompatActivity {

    private MapView map = null;
    private DatabaseHelper dbHelper;
    private ArrayList<ShelterModel> shelterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load configuration for osmdroid
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_shelters_map);

        dbHelper = new DatabaseHelper(this);
        shelterList = new ArrayList<>();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(10.0);

        loadShelters();
        displayShelters();
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

                if (lat != 0.0 || lng != 0.0) {
                    shelterList.add(new ShelterModel(name, email, contact, city, medInfo, lat, lng));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void displayShelters() {
        if (shelterList.isEmpty()) {
            GeoPoint startPoint = new GeoPoint(20.5937, 78.9629); // India
            map.getController().setCenter(startPoint);
            map.getController().setZoom(5.0);
            return;
        }

        for (ShelterModel shelter : shelterList) {
            GeoPoint point = new GeoPoint(shelter.getLatitude(), shelter.getLongitude());
            Marker startMarker = new Marker(map);
            startMarker.setPosition(point);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            startMarker.setTitle(shelter.getName());
            startMarker.setSnippet(shelter.getCity() + " | " + shelter.getContact());
            map.getOverlays().add(startMarker);
        }

        // Focus on the first shelter
        GeoPoint firstPoint = new GeoPoint(shelterList.get(0).getLatitude(), shelterList.get(0).getLongitude());
        map.getController().setCenter(firstPoint);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }
}
