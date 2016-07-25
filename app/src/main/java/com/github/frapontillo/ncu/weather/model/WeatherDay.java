package com.github.frapontillo.ncu.weather.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.Date;

@AutoValue
public abstract class WeatherDay implements Parcelable {

    public static Builder builder() {
        return new AutoValue_WeatherDay.Builder();
    }

    public abstract Date date();

    public abstract int weatherId();

    public abstract String weatherDescription();

    public abstract double pressure();

    public abstract double humidity();

    public abstract double minTemperature();

    public abstract double maxTemperature();

    public abstract double windDirection();

    public abstract double windSpeed();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder date(Date date);

        public abstract Builder weatherId(int weatherId);

        public abstract Builder weatherDescription(String description);

        public abstract Builder pressure(double pressure);

        public abstract Builder humidity(double humidity);

        public abstract Builder minTemperature(double minTemperature);

        public abstract Builder maxTemperature(double maxTemperature);

        public abstract Builder windDirection(double windDirection);

        public abstract Builder windSpeed(double windSpeed);

        public abstract WeatherDay build();

    }

}
