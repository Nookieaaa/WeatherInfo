package com.nookdev.weathertesttask.models.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coord {
    @SerializedName("lon")
    @Expose
    public Double lon;

    @SerializedName("lat")
    @Expose
    public Double lat;

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }


}
