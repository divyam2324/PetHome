package com.pethome;

import com.google.gson.annotations.SerializedName;

public class PredictResponse {
    @SerializedName("symptoms")
    private String symptoms;

    @SerializedName("disease")
    private String disease;

    @SerializedName("urgency")
    private String urgency;

    public String getSymptoms() {
        return symptoms;
    }

    public String getDisease() {
        return disease;
    }

    public String getUrgency() {
        return urgency;
    }
}
