package com.github.frapontillo.ncu.data;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.github.frapontillo.ncu.data.contract.WeatherContract;
import com.github.frapontillo.ncu.weather.openweather.WeatherForecast;
import com.github.frapontillo.ncu.weather.openweather.WeatherForecastDay;

import java.util.List;

public class WeatherForecastPersister {

    private final ContentResolver contentResolver;
    private final WeatherContract weatherContract;

    public WeatherForecastPersister(ContentResolver contentResolver, WeatherContract weatherContract) {
        this.contentResolver = contentResolver;
        this.weatherContract = weatherContract;
    }

    public void persistWeatherForecast(WeatherForecast weatherForecast, long locationId) {
        if (weatherForecast == null) {
            return;
        }

        List<WeatherForecastDay> weatherForecastDays = weatherForecast.days();
        ContentValues[] daysValues = weatherContract.toContentValues(weatherForecastDays, locationId);
        contentResolver.bulkInsert(WeatherContract.CONTENT_URI, daysValues);
    }

}
