package com.github.frapontillo.ncu.weather.service;

import android.content.ContentResolver;
import android.content.Context;

import com.github.frapontillo.ncu.Clock;
import com.github.frapontillo.ncu.SystemClock;
import com.github.frapontillo.ncu.data.WeatherForecastDatabaseLayer;
import com.github.frapontillo.ncu.data.WeatherLocationDatabaseLayer;
import com.github.frapontillo.ncu.data.contract.LocationContract;
import com.github.frapontillo.ncu.data.contract.WeatherContract;
import com.github.frapontillo.ncu.weather.model.Weather;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

class WeatherLocalDataSource implements WeatherDataSource {

    private final WeatherForecastDatabaseLayer weatherForecastDatabaseLayer;
    private final WeatherLocationDatabaseLayer weatherLocationDatabaseLayer;
    private final Clock clock;

    static WeatherLocalDataSource newInstance(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        WeatherForecastDatabaseLayer weatherForecastDatabaseLayer = new WeatherForecastDatabaseLayer(contentResolver, new WeatherContract());
        WeatherLocationDatabaseLayer weatherLocationDatabaseLayer = new WeatherLocationDatabaseLayer(contentResolver, new LocationContract());

        return new WeatherLocalDataSource(weatherForecastDatabaseLayer, weatherLocationDatabaseLayer, new SystemClock());
    }

    private WeatherLocalDataSource(WeatherForecastDatabaseLayer weatherForecastDatabaseLayer,
                                   WeatherLocationDatabaseLayer weatherLocationDatabaseLayer,
                                   Clock clock) {

        this.weatherForecastDatabaseLayer = weatherForecastDatabaseLayer;
        this.weatherLocationDatabaseLayer = weatherLocationDatabaseLayer;
        this.clock = clock;
    }

    @Override
    public Observable<Weather> getWeather(String zipCode) {
        List<Weather> latestWeatherList = Collections.emptyList();

        Date todayAtMidnight = clock.getTodayAtMidnight();
        Weather latestWeather = weatherForecastDatabaseLayer.getLatestWeatherFor(zipCode, todayAtMidnight);
        if (latestWeather != null) {
            latestWeatherList = Collections.singletonList(latestWeather);
        }

        return Observable.from(latestWeatherList);
    }

    @Override
    public Func1<Weather, Weather> persistWeather() {
        return new Func1<Weather, Weather>() {
            @Override
            public Weather call(Weather weather) {
                long locationId = weatherLocationDatabaseLayer.saveLocationAndGetId(weather.location());
                return weatherForecastDatabaseLayer.persistWeatherForecast(weather, locationId);
            }
        };
    }

}
