package com.pethome;

import com.google.gson.annotations.SerializedName;

public class PredictRequest {
    @SerializedName("symptoms")
    private String symptoms;

    public PredictRequest(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}
