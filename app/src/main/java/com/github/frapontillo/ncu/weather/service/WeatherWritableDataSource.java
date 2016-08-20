package com.github.frapontillo.ncu.weather.service;

import com.github.frapontillo.ncu.weather.model.Weather;

interface WeatherWritableDataSource {

    Weather persistWeather(Weather weather);

}
