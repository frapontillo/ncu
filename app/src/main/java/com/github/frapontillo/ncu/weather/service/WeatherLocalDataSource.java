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

import java.util.Date;
import java.util.List;

import rx.Observable;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

class WeatherLocalDataSource implements WeatherReadableDataSource, WeatherWritableDataSource {

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
        List<Weather> latestWeatherList = emptyList();

        Date todayAtMidnight = clock.getTodayAtMidnight();
        Weather latestWeather = weatherForecastDatabaseLayer.getLatestWeatherFor(zipCode, todayAtMidnight);
        if (latestWeather != null) {
            latestWeatherList = singletonList(latestWeather);
        }

        return Observable.from(latestWeatherList);
    }

    @Override
    public boolean retrievesFreshData() {
        return false;
    }

    @Override
    public Weather persistWeather(Weather weather) {
        long locationId = weatherLocationDatabaseLayer.saveLocationAndGetId(weather.location());
        return weatherForecastDatabaseLayer.persistWeatherForecast(weather, locationId);
    }

}
