package com.github.frapontillo.ncu.weather.service;

import com.github.frapontillo.ncu.weather.model.Weather;

import rx.Observable;

interface WeatherReadableDataSource {

    Observable<Weather> getWeather(String zipCode);

    boolean retrievesFreshData();

}
