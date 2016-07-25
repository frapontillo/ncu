package com.github.frapontillo.ncu.weather.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class ApiWeatherForecastDay {

    public static TypeAdapter<ApiWeatherForecastDay> typeAdapter(Gson gson) {
        return new AutoValue_ApiWeatherForecastDay.GsonTypeAdapter(gson);
    }

    @SerializedName("dt")
    public abstract long date();

    @SerializedName("temp")
    public abstract ApiWeatherForecastDayTemperature temperatures();

    @SerializedName("weather")
    public abstract List<ApiWeatherForecastDayCondition> conditions();

    public abstract double pressure();

    public abstract double humidity();

    @SerializedName("speed")
    public abstract double windSpeed();

    @SerializedName("deg")
    public abstract double windDirection();

    public abstract double clouds();

}
