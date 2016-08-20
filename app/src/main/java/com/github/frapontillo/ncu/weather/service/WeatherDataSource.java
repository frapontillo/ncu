package com.github.frapontillo.ncu.weather.service;

import com.github.frapontillo.ncu.weather.model.Weather;

import rx.Observable;
import rx.functions.Func1;

interface WeatherDataSource {

    Observable<Weather> getWeather(String zipCode);

    Func1<Weather, Weather> persistWeather();

}
