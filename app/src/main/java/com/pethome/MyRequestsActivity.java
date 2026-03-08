package com.pethome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRequestsActivity extends AppCompatActivity {

    RecyclerView recyclerRequests;
    DatabaseHelper dbHelper;
    ArrayList<RequestModel> requestList;
    RequestAdapter adapter;
    TextView txtNoRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        recyclerRequests = findViewById(R.id.ownrecyclerRequests);
        txtNoRequests = findViewById(R.id.txtNoRequests);
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        dbHelper = new DatabaseHelper(this);
        requestList = new ArrayList<>();

        recyclerRequests.setLayoutManager(new LinearLayoutManager(this));

        loadRequests();
    }

    private void loadRequests() {
        requestList.clear();
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = prefs.getString("email", null);

        if (userEmail == null) return;

        Cursor cursor = dbHelper.getRequestsByUser(userEmail);

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

                requestList.add(new RequestModel(id, petId, name, breed, age, gender, image, requestUser, status));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (requestList.isEmpty()) {
            txtNoRequests.setVisibility(View.VISIBLE);
            recyclerRequests.setVisibility(View.GONE);
        } else {
            txtNoRequests.setVisibility(View.GONE);
            recyclerRequests.setVisibility(View.VISIBLE);
        }

        adapter = new RequestAdapter(requestList, false, null);
        recyclerRequests.setAdapter(adapter);
    }
}
