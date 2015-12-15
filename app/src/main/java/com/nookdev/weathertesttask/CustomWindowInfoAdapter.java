package com.nookdev.weathertesttask;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.nookdev.weathertesttask.models.ResponseObject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CustomWindowInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private final View contentView;
    @Bind(R.id.weather_marker_cloud)
    TextView clouds;
    @Bind(R.id.weather_marker_hum)
    TextView humidity;
    @Bind(R.id.weather_marker_image)
    ImageView image;
    @Bind(R.id.weather_marker_pressure)
    TextView pressure;
    @Bind(R.id.weather_marker_temp)
    TextView temperature;
    @Bind(R.id.weather_marker_sunrise)
    TextView sunrise;
    @Bind(R.id.weather_marker_sunset)
    TextView sunset;
    private Bitmap icon;

    public CustomWindowInfoAdapter() {
        LayoutInflater inflater = (LayoutInflater)App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contentView = inflater.inflate(R.layout.weather_data_infowindow, null);
        ButterKnife.bind(this, contentView);
    }

    public void update(ResponseObject weatherData){
        clouds.setText("WOW");
        image.setImageBitmap(weatherData.getBitmap());

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return contentView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
