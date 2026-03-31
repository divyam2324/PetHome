package com.pethome;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("predict")
    Call<PredictResponse> predictDisease(@Body PredictRequest request);
}
