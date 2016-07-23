package com.github.frapontillo.ncu.weather;

import com.github.frapontillo.ncu.weather.openweather.WeatherForecast;

import rx.Observable;

public class WeatherServiceClient {

    private final WeatherService weatherService;

    public WeatherServiceClient(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public Observable<WeatherForecast> getWeekForecast(String query) {
        return weatherService.getDailyForecast(query, "json", "metric", 7);
    }

}
