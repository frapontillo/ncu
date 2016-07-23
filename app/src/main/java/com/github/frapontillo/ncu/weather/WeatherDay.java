package com.github.frapontillo.ncu.weather;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.text.SimpleDateFormat;
import java.util.Date;

@AutoValue
public abstract class WeatherDay implements Parcelable {
    public abstract Date date();

    public abstract long weatherId();

    public abstract String description();

    public abstract double high();

    public abstract double low();

    public abstract int humidity();

    public abstract double pressure();

    public abstract double windDirection();

    public abstract double windSpeed();

    static WeatherDay create(Date date,
                             long weatherId,
                             String description,
                             double high,
                             double low,
                             int humidity,
                             double pressure,
                             double windDirection,
                             double windSpeed) {
        return new AutoValue_WeatherDay(date, weatherId, description, high, low, humidity, pressure, windDirection, windSpeed);
    }

    public String day() {
        return SimpleDateFormat.getDateInstance().format(date());
    }

    private double convertToFahrenheitMaybe(double celsius, boolean asImperial) {
        if (asImperial) {
            return (celsius * 1.8D) + 32;
        }
        return celsius;
    }

    public double high(boolean asImperial) {
        return convertToFahrenheitMaybe(high(), asImperial);
    }

    public double low(boolean asImperial) {
        return convertToFahrenheitMaybe(low(), asImperial);
    }
}
