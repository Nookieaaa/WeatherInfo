package com.nookdev.weathertesttask.api;


import com.nookdev.weathertesttask.models.ResponseObject;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface OpenWeatherApi {
    @GET("/data/2.5/weather")
    Call<ResponseObject> getForecast(@Query("q") String city);

}

