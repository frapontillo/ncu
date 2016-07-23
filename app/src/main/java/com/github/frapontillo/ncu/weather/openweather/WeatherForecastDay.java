package com.github.frapontillo.ncu.weather.openweather;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@AutoValue
public abstract class WeatherForecastDay {

    public static TypeAdapter<WeatherForecastDay> typeAdapter(Gson gson) {
        return new AutoValue_WeatherForecastDay.GsonTypeAdapter(gson);
    }

    @SerializedName("dt")
    public abstract Date date();

    @SerializedName("temp")
    public abstract WeatherForecastDayTemperature temperatures();

    @SerializedName("weather")
    public abstract List<WeatherForecastDayCondition> conditions();

    public abstract long pressure();

    public abstract long humidity();

    @SerializedName("speed")
    public abstract long windSpeed();

    @SerializedName("deg")
    public abstract long windDirection();

    public abstract long clouds();

}
