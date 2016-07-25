package com.github.frapontillo.ncu.weather.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class ApiWeatherForecast {

    public static TypeAdapter<ApiWeatherForecast> typeAdapter(Gson gson) {
        return new AutoValue_ApiWeatherForecast.GsonTypeAdapter(gson);
    }

    public abstract ApiWeatherCity city();

    @SerializedName("list")
    public abstract List<ApiWeatherForecastDay> days();

}
