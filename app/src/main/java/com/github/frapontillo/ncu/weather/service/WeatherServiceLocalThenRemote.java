package com.github.frapontillo.ncu.weather.service;

import android.content.Context;

import com.github.frapontillo.ncu.weather.model.Event;
import com.github.frapontillo.ncu.weather.model.Weather;
import com.jakewharton.rxrelay.BehaviorRelay;

import rx.Observable;
import rx.functions.Action0;

import static com.github.frapontillo.ncu.weather.service.Helper.asEvent;

public class WeatherServiceLocalThenRemote implements WeatherService {

    private final WeatherRemoteDataSource weatherRemoteDataSource;
    private final WeatherLocalDataSource weatherLocalDataSource;

    private final BehaviorRelay<Event<Weather>> weatherRelay;

    public static WeatherServiceLocalThenRemote newInstance(Context context) {
        return new WeatherServiceLocalThenRemote(
                WeatherRemoteDataSource.newInstance(),
                WeatherLocalDataSource.newInstance(context)
        );
    }

    private WeatherServiceLocalThenRemote(WeatherRemoteDataSource weatherRemoteDataSource,
                                          WeatherLocalDataSource weatherLocalDataSource) {
        this.weatherRemoteDataSource = weatherRemoteDataSource;
        this.weatherLocalDataSource = weatherLocalDataSource;

        this.weatherRelay = BehaviorRelay.create(Event.<Weather>idle());
    }

    @Override
    public Observable<Event<Weather>> getWeather(String zipCode) {
        return weatherRelay.asObservable()
                .startWith(initialiseFromLocalOrRemote(zipCode))
                .distinctUntilChanged();
    }

    private Observable<Event<Weather>> initialiseFromLocalOrRemote(String zipCode) {
        return weatherLocalDataSource
                .getWeather(zipCode)
                .switchIfEmpty(getWeatherFromRemote(zipCode))
                .compose(asEvent())
                .doOnNext(weatherRelay);

    }

    @Override
    public Action0 refreshWeather(final String zipCode) {
        return new Action0() {
            @Override
            public void call() {
                getWeatherFromRemote(zipCode)
                        .compose(asEvent())
                        .subscribe(weatherRelay);
            }
        };
    }

    private Observable<Weather> getWeatherFromRemote(String zipCode) {
        return weatherRemoteDataSource
                .getWeather(zipCode)
                .map(weatherLocalDataSource.persistWeather());
    }

}
