package com.pethome;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<RequestModel> historyList;
    RequestAdapter adapter;
    DatabaseHelper dbHelper;
    TextView txtNoHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerHistory);
        txtNoHistory = findViewById(R.id.txtNoHistory);
        
        if (recyclerView == null) {
            // If the layout doesn't have recyclerHistory yet, we need to update activity_history.xml
            return;
        }
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        historyList = new ArrayList<>();

        loadHistory();
    }

    private void loadHistory() {
        historyList.clear();
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", null);
        String role = prefs.getString("role", "User");

        if (email == null) return;

        boolean isShelter = "NGO / Shelter".equals(role);
        Cursor cursor = dbHelper.getAdoptionHistory(email, isShelter);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                int petId = cursor.getInt(cursor.getColumnIndexOrThrow("PETID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("PETNAME"));
                String breed = cursor.getString(cursor.getColumnIndexOrThrow("BREED"));
                String age = cursor.getString(cursor.getColumnIndexOrThrow("AGE"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("GENDER"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("IMAGE"));
                String requestUser = cursor.getString(cursor.getColumnIndexOrThrow("REQUESTUSER"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("STATUS"));

                historyList.add(new RequestModel(id, petId, name, breed, age, gender, image, requestUser, status));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (historyList.isEmpty()) {
            txtNoHistory.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtNoHistory.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter = new RequestAdapter(historyList, false, null);
        recyclerView.setAdapter(adapter);
    }
}
