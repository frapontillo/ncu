package com.github.frapontillo.ncu.weather.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class WeatherLocation implements Parcelable {

    public static Builder builder() {
        return new $AutoValue_WeatherLocation.Builder();
    }

    public abstract String zipCode();

    public abstract String cityName();

    public abstract double latitude();

    public abstract double longitude();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder zipCode(String zipCode);

        public abstract Builder cityName(String cityName);

        public abstract Builder latitude(double latitude);

        public abstract Builder longitude(double longitude);

        public abstract WeatherLocation build();

    }

}
