package com.github.frapontillo.ncu.weather.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class ApiWeatherForecastDayTemperature {

    public static TypeAdapter<ApiWeatherForecastDayTemperature> typeAdapter(Gson gson) {
        return new AutoValue_ApiWeatherForecastDayTemperature.GsonTypeAdapter(gson);
    }

    public abstract double day();

    public abstract double min();

    public abstract double max();

    public abstract double night();

    public abstract double eve();

    @SerializedName("morn")
    public abstract double morning();

}
