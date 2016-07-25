package com.github.frapontillo.ncu.weather.model;

import java.text.SimpleDateFormat;

public class WeatherDayFormatter {

    private final WeatherDay weatherDay;

    public WeatherDayFormatter(WeatherDay weatherDay) {
        this.weatherDay = weatherDay;
    }

    public String getDay() {
        return SimpleDateFormat.getDateInstance().format(weatherDay.date());
    }

    public double getMaxTemperature(boolean asImperial) {
        return convertToFahrenheitMaybe(weatherDay.maxTemperature(), asImperial);
    }

    public double getMinTemperature(boolean asImperial) {
        return convertToFahrenheitMaybe(weatherDay.minTemperature(), asImperial);
    }

    private double convertToFahrenheitMaybe(double celsius, boolean asImperial) {
        if (asImperial) {
            return (celsius * 1.8D) + 32;
        }
        return celsius;
    }

}
