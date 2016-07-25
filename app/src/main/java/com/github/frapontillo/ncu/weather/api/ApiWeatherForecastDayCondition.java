package com.github.frapontillo.ncu.weather.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class ApiWeatherForecastDayCondition {

    public static TypeAdapter<ApiWeatherForecastDayCondition> typeAdapter(Gson gson) {
        return new AutoValue_ApiWeatherForecastDayCondition.GsonTypeAdapter(gson);
    }

    public abstract int id();

    @SerializedName("main")
    public abstract String name();

    public abstract String description();

    @SerializedName("icon")
    public abstract String iconResource();

}
