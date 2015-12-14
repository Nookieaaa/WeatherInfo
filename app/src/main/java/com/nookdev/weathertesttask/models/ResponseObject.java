package com.nookdev.weathertesttask.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nookdev.weathertesttask.models.pojo.Clouds;
import com.nookdev.weathertesttask.models.pojo.Coord;
import com.nookdev.weathertesttask.models.pojo.Main;
import com.nookdev.weathertesttask.models.pojo.Sys;
import com.nookdev.weathertesttask.models.pojo.Weather;
import com.nookdev.weathertesttask.models.pojo.Wind;

import java.util.ArrayList;
import java.util.List;

public class ResponseObject {
    @SerializedName("coord")
    @Expose
    public Coord coord;
    @SerializedName("weather")
    @Expose
    public List<Weather> weather = new ArrayList<Weather>();
    @SerializedName("base")
    @Expose
    public String base;
    @SerializedName("main")
    @Expose
    public Main main;
    @SerializedName("wind")
    @Expose
    public Wind wind;
    @SerializedName("clouds")
    @Expose
    public Clouds clouds;
    @SerializedName("dt")
    @Expose
    public Integer dt;
    @SerializedName("sys")
    @Expose
    public Sys sys;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("cod")
    @Expose
    public Integer cod;

}
