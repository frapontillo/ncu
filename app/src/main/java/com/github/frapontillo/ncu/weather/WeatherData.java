package com.github.frapontillo.ncu.weather;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class WeatherData implements Parcelable {
    public abstract String day();
    public abstract String description();
    abstract double high();
    abstract double low();

    static WeatherData create(String day, String description, double high, double low) {
        return new AutoValue_WeatherData(day, description, high, low);
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
