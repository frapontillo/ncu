package com.github.frapontillo.ncu.weather;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class WeatherLocation implements Parcelable {
    public abstract String zipCode();

    public abstract String cityName();

    public abstract long latitude();

    public abstract long longitude();

    static WeatherLocation create(String zipCode, String cityName, long latitude, long longitude) {
        return new AutoValue_WeatherLocation(zipCode, cityName, latitude, longitude);
    }
}
