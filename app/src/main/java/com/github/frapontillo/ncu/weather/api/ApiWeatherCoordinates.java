package com.github.frapontillo.ncu.weather.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class ApiWeatherCoordinates {

    public static TypeAdapter<ApiWeatherCoordinates> typeAdapter(Gson gson) {
        return new AutoValue_ApiWeatherCoordinates.GsonTypeAdapter(gson);
    }

    @SerializedName("lat")
    public abstract double latitude();

    @SerializedName("lon")
    public abstract double longitude();

}
