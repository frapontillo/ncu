package com.github.frapontillo.ncu.navigator;

import com.github.frapontillo.ncu.weather.model.WeatherDay;

public interface Navigator {

    void toWeatherDay(WeatherDay weatherDay);

    void toSettings();

    void openMaps(String zipCode);
}
