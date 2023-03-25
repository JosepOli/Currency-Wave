package com.example.currencywave.api;

import com.example.currencywave.model.CurrencyApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyApi {

    @GET("04f5a4f9b05387a94a2f4e57/latest/USD")
    Call<CurrencyApiResponse> getLatestExchangeRates(
            @Query("base") String baseCurrency,
            @Query("access_key") String apiKey
    );
}