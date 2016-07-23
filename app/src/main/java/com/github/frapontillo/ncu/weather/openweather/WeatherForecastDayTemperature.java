package com.github.frapontillo.ncu.weather.openweather;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class WeatherForecastDayTemperature {

    public static TypeAdapter<WeatherForecastDayTemperature> typeAdapter(Gson gson) {
        return new AutoValue_WeatherForecastDayTemperature.GsonTypeAdapter(gson);
    }

    public abstract long day();

    public abstract long min();

    public abstract long max();

    public abstract long night();

    public abstract long eve();

    @SerializedName("morn")
    public abstract long morning();

}
