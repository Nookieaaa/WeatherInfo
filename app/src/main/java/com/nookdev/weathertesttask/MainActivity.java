package com.nookdev.weathertesttask;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.nookdev.weathertesttask.api.OpenWeatherApi;
import com.nookdev.weathertesttask.models.ResponseObject;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://api.openweathermap.org";
    public static final String APPID = "25908398e09c807a0023e7c4f387d1e6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queryWeather("kiev");
    }

    public void queryWeather(String city){
        Interceptor interceptor = new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                String currentUrlString = chain.request().urlString();
                Request request = chain.request()
                        .newBuilder()
                        .url(currentUrlString + "&appid=" + APPID)
                        .build();
                return chain.proceed(request);
            }
        };

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(interceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        OpenWeatherApi api = retrofit.create(OpenWeatherApi.class);
        Call<ResponseObject> answer = api.getForecast(city);
        answer.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Response<ResponseObject> response, Retrofit retrofit) {
                ResponseObject weatherData = response.body();
                Snackbar.make(findViewById(R.id.rl),"got result",Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    };


}
