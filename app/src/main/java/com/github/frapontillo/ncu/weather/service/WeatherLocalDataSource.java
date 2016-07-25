package com.github.frapontillo.ncu.weather.service;

import android.content.ContentResolver;
import android.content.Context;

import com.github.frapontillo.ncu.data.WeatherForecastDatabaseLayer;
import com.github.frapontillo.ncu.data.WeatherLocationDatabaseLayer;
import com.github.frapontillo.ncu.data.contract.LocationContract;
import com.github.frapontillo.ncu.data.contract.WeatherContract;
import com.github.frapontillo.ncu.weather.model.Weather;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class WeatherLocalDataSource {

    private final WeatherForecastDatabaseLayer weatherForecastDatabaseLayer;
    private final WeatherLocationDatabaseLayer weatherLocationDatabaseLayer;

    public static WeatherLocalDataSource newInstance(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        WeatherForecastDatabaseLayer weatherForecastDatabaseLayer = new WeatherForecastDatabaseLayer(contentResolver, new WeatherContract());
        WeatherLocationDatabaseLayer weatherLocationDatabaseLayer = new WeatherLocationDatabaseLayer(contentResolver, new LocationContract());

        return new WeatherLocalDataSource(weatherForecastDatabaseLayer, weatherLocationDatabaseLayer);
    }

    public WeatherLocalDataSource(WeatherForecastDatabaseLayer weatherForecastDatabaseLayer,
                                  WeatherLocationDatabaseLayer weatherLocationDatabaseLayer) {

        this.weatherForecastDatabaseLayer = weatherForecastDatabaseLayer;
        this.weatherLocationDatabaseLayer = weatherLocationDatabaseLayer;
    }

    public Func1<Weather, Weather> persistWeather() {
        return new Func1<Weather, Weather>() {
            @Override
            public Weather call(Weather weather) {
                long locationId = weatherLocationDatabaseLayer.saveLocationAndGetId(weather.location());
                return weatherForecastDatabaseLayer.persistWeatherForecast(weather, locationId);
            }
        };
    }

    public Observable<Weather> getLatestWeatherFor(String zipCode) {
        List<Weather> latestWeatherList = Collections.emptyList();

        Weather latestWeather = weatherForecastDatabaseLayer.getLatestWeatherFor(zipCode);
        if (latestWeather != null) {
            latestWeatherList = Collections.singletonList(latestWeather);
        }

        return Observable.from(latestWeatherList);
    }
}
