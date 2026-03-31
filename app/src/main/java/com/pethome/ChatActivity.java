package com.pethome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private EditText editSymptoms;
    private ImageButton btnSend;
    private LinearLayout chatLayout;
    private ScrollView chatScrollView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editSymptoms = findViewById(R.id.editSymptoms);
        btnSend = findViewById(R.id.btnSend);
        chatLayout = findViewById(R.id.chatLayout);
        chatScrollView = findViewById(R.id.chatScrollView);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Set initial bot message only if chatLayout is empty
        if (chatLayout.getChildCount() <= 1) { // 1 because of the "Today" TextView
             addBotMessage("Hello! I'm your PetCare AI. Describe your pet's symptoms, and I'll try to help.");
        }

        btnSend.setOnClickListener(v -> {
            String symptoms = editSymptoms.getText().toString().trim();
            if (symptoms.isEmpty()) {
                Toast.makeText(this, "Please enter symptoms", Toast.LENGTH_SHORT).show();
                return;
            }
            addUserMessage(symptoms);
            getPrediction(symptoms);
            editSymptoms.setText("");
        });
    }

    private void addUserMessage(String message) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_chat_user, chatLayout, false);
        TextView textMessage = view.findViewById(R.id.textMessage);
        TextView textTime = view.findViewById(R.id.textTime);
        
        textMessage.setText(message);
        textTime.setText(new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date()));
        
        chatLayout.addView(view);
        scrollToBottom();
    }

    private void addBotMessage(String message) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_chat_bot, chatLayout, false);
        TextView textMessage = view.findViewById(R.id.textMessage);
        TextView textTime = view.findViewById(R.id.textTime);
        
        textMessage.setText(message);
        textTime.setText(new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date()));
        
        chatLayout.addView(view);
        scrollToBottom();
    }

    private void scrollToBottom() {
        chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void getPrediction(String symptoms) {
        setLoading(true);
        PredictRequest request = new PredictRequest(symptoms);

        RetrofitClient.getApiService().predictDisease(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PredictResponse> call, @NonNull Response<PredictResponse> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    PredictResponse res = response.body();
                    String botReply = "Disease: " + res.getDisease() + "\nUrgency: " + res.getUrgency() + "\nAdvice: Consult a veterinarian.";
                    addBotMessage(botReply);
                } else {
                    addBotMessage("Sorry, I couldn't analyze that. Please try again later.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PredictResponse> call, @NonNull Throwable t) {
                setLoading(false);
                addBotMessage("Error: " + t.getMessage());
            }
        });
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnSend.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        editSymptoms.setEnabled(!isLoading);
    }
}
