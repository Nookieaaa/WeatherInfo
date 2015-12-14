package com.nookdev.weathertesttask.models.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    @Expose
    public Double speed;
    @SerializedName("deg")
    @Expose
    public Integer deg;

    public Double getSpeed() {
        return speed;
    }

    public Integer getDeg() {
        return deg;
    }
}
