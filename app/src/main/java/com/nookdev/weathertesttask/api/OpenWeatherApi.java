package com.nookdev.weathertesttask.api;


import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface OpenWeatherApi {
    @GET("/data/2.5/weather")
    Call<ResponseBody> getForecast(@Query("q") String city);

}

