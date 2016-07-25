package com.github.frapontillo.ncu;

import android.app.Application;
import android.preference.PreferenceManager;

import com.github.frapontillo.ncu.weather.service.WeatherService;
import com.github.frapontillo.ncu.weather.service.WeatherServiceAsync;
import com.github.frapontillo.ncu.weather.service.WeatherServicePersisted;

public class SunshineApp extends Application {

    private WeatherService weatherService;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        weatherService = new WeatherServiceAsync(
                WeatherServicePersisted.newInstance(getApplicationContext())
        );
    }

    public WeatherService getWeatherService() {
        return weatherService;
    }
}
