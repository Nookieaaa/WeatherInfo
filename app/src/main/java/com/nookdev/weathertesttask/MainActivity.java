package com.nookdev.weathertesttask;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nookdev.weathertesttask.api.OpenWeatherApi;
import com.nookdev.weathertesttask.models.ResponseObject;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.Locale;

import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String BASE_URL = "http://api.openweathermap.org";
    public static final String APPID = "25908398e09c807a0023e7c4f387d1e6";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setCustomView(R.layout.search_bar);
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_SHOW_HOME);
            ab.setIcon(R.mipmap.ic_launcher);
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        queryWeather("kiev");
    }

    public void queryWeather(String city){
        Interceptor interceptor = new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                String currentUrlString = chain.request().urlString();
                Request request = chain.request()
                        .newBuilder()
                        .url(currentUrlString + "&lang="+getLocale()+ "&units=metric" + "&appid=" + APPID)
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
                dataReceived(weatherData);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private String getLocale(){
        return Locale.getDefault().getLanguage();
    }

    public void dataReceived(ResponseObject weatherData){
        LatLng newPos = new LatLng(weatherData.getCoord().getLat(),weatherData.getCoord().getLon());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(newPos).title("new position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newPos));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
