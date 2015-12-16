package com.nookdev.weathertesttask;

import android.content.Context;
import android.content.res.Resources;
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
    @Bind(R.id.weather_marker_location)
    TextView location;
    @Bind(R.id.weather_marker_condition)
    TextView condition;
    @Bind(R.id.weather_marker_temp_minmax)
    TextView tempMinMax;
    @Bind(R.id.weather_marker_cloud)
    TextView clouds;
    @Bind(R.id.weather_marker_hum)
    TextView humidity;
    @Bind(R.id.weather_marker_image)
    ImageView image;
    @Bind(R.id.weather_marker_wind)
    TextView wind;
    @Bind(R.id.weather_marker_pressure)
    TextView pressure;
    @Bind(R.id.weather_marker_temp)
    TextView temperature;

    private Bitmap icon;

    public CustomWindowInfoAdapter() {
        LayoutInflater inflater = (LayoutInflater)App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contentView = inflater.inflate(R.layout.weather_data_infowindow, null);
        ButterKnife.bind(this, contentView);
    }

    public void update(ResponseObject weatherData){
        Resources res = App.getAppContext().getResources();

        location.setText(String.format(res.getString(R.string.location),weatherData.getName(),weatherData.getSys().getCountry()));
        temperature.setText(String.format(res.getString(R.string.temp),weatherData.getMain().getTemp().intValue()));

        int tempMin = 0;
        int tempMax = 0;
        boolean minIsNull = false;
        boolean maxIsNull = false;

        if(weatherData.getMain().getTempMax()!=null)
            tempMax = weatherData.getMain().getTempMax().intValue();
        else
            maxIsNull = true;

        if(weatherData.getMain().getTempMin()!=null)
            tempMin = weatherData.getMain().getTempMin().intValue();
        else
            minIsNull = true;

        if(minIsNull&&!maxIsNull)
            tempMin = tempMax;
        if (maxIsNull&&!minIsNull)
            tempMax = tempMin;

        tempMinMax.setText(String.format(res.getString(R.string.temp_minmax), tempMin, tempMax));
        condition.setText(weatherData.getWeather().get(0).getDescription());
        pressure.setText(String.format(res.getString(R.string.pressure), weatherData.getMain().getPressure()));
        humidity.setText(String.format(res.getString(R.string.humidity), weatherData.getMain().getHumidity(),"%"));

        wind.setText(String.format(res.getString(R.string.wind), weatherData.getWind().getSpeed().intValue(), weatherData.getWind().getDeg().intValue()));
        clouds.setText(String.format(res.getString(R.string.clouds), weatherData.getClouds().getAll(), "%"));

        Bitmap icon = weatherData.getBitmap();
        if(icon!=null)
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
