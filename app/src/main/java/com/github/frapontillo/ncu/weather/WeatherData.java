package com.github.frapontillo.ncu.weather;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class WeatherData implements Parcelable {
    public abstract WeatherLocation weatherLocation();

    public abstract List<WeatherDay> weatherDays();

    static WeatherData create(WeatherLocation weatherLocation, List<WeatherDay> weatherDays) {
        return new AutoValue_WeatherData(weatherLocation, weatherDays);
    }
}
