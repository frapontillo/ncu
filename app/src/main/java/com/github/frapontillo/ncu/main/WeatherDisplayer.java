package com.github.frapontillo.ncu.main;

import com.github.frapontillo.ncu.weather.model.Weather;

public interface WeatherDisplayer {

    void display(Weather weather);

    void attach(WeatherActionListener weatherActionListener);

    void detach(WeatherActionListener weatherActionListener);

}
