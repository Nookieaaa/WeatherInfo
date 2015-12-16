package com.nookdev.weathertesttask.models;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nookdev.weathertesttask.App;
import com.nookdev.weathertesttask.models.pojo.Clouds;
import com.nookdev.weathertesttask.models.pojo.Coord;
import com.nookdev.weathertesttask.models.pojo.Main;
import com.nookdev.weathertesttask.models.pojo.Sys;
import com.nookdev.weathertesttask.models.pojo.Weather;
import com.nookdev.weathertesttask.models.pojo.Wind;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class ResponseObject {
    @SerializedName("coord")
    @Expose
    private Coord coord;
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = new ArrayList<>();
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("clouds")
    @Expose
    private Clouds clouds;
    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("sys")
    @Expose
    private Sys sys;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cod")
    @Expose
    private Integer cod;
    private DataReady mDataReadyListener;

    private final ImageHolder imageHolder = new ImageHolder();

    public Coord getCoord() {
        return coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Integer getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCod() {
        return cod;
    }

    public void loadImage(){
        imageHolder.requestImage();
    }

    public Bitmap getBitmap(){
        return imageHolder.getBitmap();
    }

    public void setDataReadyListener(DataReady listener){
        mDataReadyListener = listener;
        imageHolder.requestImage();
    }

    private void notifyDataLoaded(){
        if(mDataReadyListener!=null){
            mDataReadyListener.onDataReceived(this);
        }
    }


    private class ImageHolder implements Target {

        public static final String TARGET_ICON_URL = "http://openweathermap.org/img/w/";
        public static final String ICON_EXT = ".png";

        private Bitmap icon;

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            icon = bitmap;
            notifyDataLoaded();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            icon = ((BitmapDrawable)errorDrawable).getBitmap();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            icon = null;
        }

        public void requestImage(){
            if (getWeather().size()>0)
                Picasso.with(App.getAppContext()).load(TARGET_ICON_URL + getWeather().get(0).getIcon() + ICON_EXT).into(this);
        }

        @Nullable
        public Bitmap getBitmap() throws NullPointerException{
            return icon;
        }
    }

    //to notify when image is loaded
    public interface DataReady{
        public void onDataReceived(ResponseObject responseObject);
    }

}
