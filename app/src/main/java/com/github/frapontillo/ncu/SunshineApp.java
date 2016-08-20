package com.github.frapontillo.ncu;

import android.app.Application;
import android.preference.PreferenceManager;

import com.github.frapontillo.ncu.weather.service.WeatherService;
import com.github.frapontillo.ncu.weather.service.WeatherServiceAsync;
import com.github.frapontillo.ncu.weather.service.WeatherServicePipeline;
import com.squareup.leakcanary.LeakCanary;

public class SunshineApp extends Application {

    private WeatherService weatherService;

    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        weatherService = new WeatherServiceAsync(
                WeatherServicePipeline.newInstance(getApplicationContext())
        );
    }

    public WeatherService getWeatherService() {
        return weatherService;
    }
}
