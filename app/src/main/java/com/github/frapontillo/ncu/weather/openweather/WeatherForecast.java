package com.github.frapontillo.ncu.weather.openweather;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class WeatherForecast {

    public abstract String city();

    @SerializedName("list")
    public abstract List<WeatherForecastDay> days();

}
