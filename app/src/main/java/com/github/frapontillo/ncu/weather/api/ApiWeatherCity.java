package com.github.frapontillo.ncu.weather.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class ApiWeatherCity {

    public static TypeAdapter<ApiWeatherCity> typeAdapter(Gson gson) {
        return new AutoValue_ApiWeatherCity.GsonTypeAdapter(gson);
    }

    public abstract String name();

    @SerializedName("coord")
    public abstract ApiWeatherCoordinates coordinates();

}
