package com.nookdev.weathertesttask;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.nookdev.weathertesttask.api.OpenWeatherApi;
import com.nookdev.weathertesttask.models.ResponseObject;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,ResponseObject.DataReady {

    public static final String BASE_URL = "http://api.openweathermap.org";
    public static final String APPID = "25908398e09c807a0023e7c4f387d1e6";
    private GoogleMap mMap;
    private CustomWindowInfoAdapter windowInfoAdapter;
    public static final String REQUERY_STATE_KEY = "requery";
    String lastQuery;
    Handler h = new Handler();

    @Bind (R.id.btn_search)
    ImageButton btnSearch;
    @Bind(R.id.search_input)
    EditText searchInput;

    @OnClick (R.id.btn_search)
    public void setBtnSearchClick(View v){
        hideSoftKeyboard();
        performSearch();
    }

    public void performSearch(){
        String query = searchInput.getText().toString();
        if(query.length()==0){
            Toast.makeText(this, "no query data",Toast.LENGTH_SHORT).show();
            return;
        }
        lastQuery = query;
        queryWeather(query);
    }

    public void hideSoftKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setCustomView(R.layout.search_bar);
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_SHOW_HOME);
            ab.setIcon(R.mipmap.ic_launcher);
            ButterKnife.bind(this, ab.getCustomView());
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (savedInstanceState!=null && savedInstanceState.containsKey(REQUERY_STATE_KEY)){
                lastQuery = savedInstanceState.getString(REQUERY_STATE_KEY);
            }

    }

    private void setupMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        windowInfoAdapter = new CustomWindowInfoAdapter();
        //mMap.setInfoWindowAdapter(windowInfoAdapter);
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
                registerDataReadyListener(weatherData);
            }

            @Override
            public void onFailure(Throwable t) {
                notifyError();
            }
        });
    }

    public void notifyError(){
        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
    }

    private String getLocale(){
        return Locale.getDefault().getLanguage();
    }

    public void registerDataReadyListener(ResponseObject weatherData){
        weatherData.setDataReadyListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(REQUERY_STATE_KEY, lastQuery);
    }

    public void dataReceived(ResponseObject weatherData){
        LatLng newPos = new LatLng(weatherData.getCoord().getLat(),weatherData.getCoord().getLon());
        mMap.clear();
        mMap.setOnMarkerClickListener(marker -> {
            //Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
            //marker.setIcon(addContentView(););
            return true;
        });
        //windowInfoAdapter.update(weatherData);
        final Marker marker = mMap.addMarker(createMarker(weatherData, newPos)
                //.icon(BitmapDescriptorFactory.fromBitmap());
        );
        CameraPosition cameraPosition = new CameraPosition.Builder().target(newPos).zoom(7f).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                //marker.showInfoWindow();
            }

            @Override
            public void onCancel() {

            }
        });
    }


    private MarkerOptions createMarker(ResponseObject weatherData, LatLng newPos){
        MarkerOptions marker = new MarkerOptions();

        /*new MarkerOptions()
                .position(newPos)
                .title(weatherData.getName());*/

        IconGenerator generator = new IconGenerator(this);
        //generator.setContentView(View.inflate(this,R.layout.custom,null));
        //generator.setBackground(getResources().getDrawable(R.drawable.circle));
        Bitmap bm = generator.makeIcon("test");

        return marker.icon(BitmapDescriptorFactory.fromBitmap(bm)).position(newPos);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupMap();
        if(lastQuery!=null){
            queryWeather(lastQuery);
        }
    }

    @Override
    public void onDataReceived(ResponseObject responseObject) {
        dataReceived(responseObject);
    }
}
