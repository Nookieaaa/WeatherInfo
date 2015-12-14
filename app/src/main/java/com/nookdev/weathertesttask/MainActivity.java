package com.nookdev.weathertesttask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nookdev.weathertesttask.api.OpenWeatherApi;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://api.openweathermap.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        OpenWeatherApi api = retrofit.create(OpenWeatherApi.class);
        Call <ResponseBody> answer = api.getForecast("kiev");
        answer.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                ResponseBody str = response.body();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }
}
