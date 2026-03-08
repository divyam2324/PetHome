package com.pethome;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OwnerRequestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<RequestModel> requestList;
    RequestAdapter adapter;
    DatabaseHelper dbHelper;
    TextView txtNoRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_requests);

        recyclerView = findViewById(R.id.recyclerRequests);
        txtNoRequests = findViewById(R.id.txtNoRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        requestList = new ArrayList<>();

        loadRequests();
    }

    private void loadRequests() {
        requestList.clear();
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String ownerEmail = prefs.getString("email", null);

        if (ownerEmail == null) return;

        Cursor cursor = dbHelper.getRequestsForOwner(ownerEmail);

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
            recyclerView.setVisibility(View.GONE);
        } else {
            txtNoRequests.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter = new RequestAdapter(requestList, true, new RequestAdapter.OnRequestActionListener() {
            @Override
            public void onApprove(RequestModel request) {
                dbHelper.approveRequest(request.getId());
                Toast.makeText(OwnerRequestActivity.this, "Request Approved", Toast.LENGTH_SHORT).show();
                loadRequests();
            }

            @Override
            public void onReject(RequestModel request) {
                dbHelper.rejectRequest(request.getId(), request.getPetId());
                Toast.makeText(OwnerRequestActivity.this, "Request Rejected", Toast.LENGTH_SHORT).show();
                loadRequests();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
