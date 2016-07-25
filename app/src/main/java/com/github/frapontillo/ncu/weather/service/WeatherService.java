package com.github.frapontillo.ncu.weather.service;

import com.github.frapontillo.ncu.weather.model.Event;
import com.github.frapontillo.ncu.weather.model.Weather;

import rx.Observable;
import rx.functions.Action0;

public interface WeatherService {

    Observable<Event<Weather>> getWeather(String zipCode);

    Action0 refreshWeather(String zipCode);

}
