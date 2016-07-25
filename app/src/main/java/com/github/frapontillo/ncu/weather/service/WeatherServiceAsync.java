package com.github.frapontillo.ncu.weather.service;

import com.github.frapontillo.ncu.weather.model.Event;
import com.github.frapontillo.ncu.weather.model.Weather;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class WeatherServiceAsync implements WeatherService {

    private final WeatherService weatherService;

    public WeatherServiceAsync(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    public Observable<Event<Weather>> getWeather(String zipCode) {
        return weatherService
                .getWeather(zipCode)
                .compose(asAsync());
    }

    @Override
    public Action0 refreshWeather(String zipCode) {
        return asAsyncAction(weatherService.refreshWeather(zipCode));
    }

    private Observable.Transformer<? super Event<Weather>, ? extends Event<Weather>> asAsync() {
        return new Observable.Transformer<Event<Weather>, Event<Weather>>() {
            @Override
            public Observable<Event<Weather>> call(Observable<Event<Weather>> eventObservable) {
                return eventObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private static Action0 asAsyncAction(final Action0 action0) {
        return new Action0() {
            @Override
            public void call() {
                asObservable(action0)
                        .subscribeOn(Schedulers.io())
                        .subscribe();
            }
        };
    }

    private static Observable<Void> asObservable(final Action0 action0) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                action0.call();
                subscriber.onCompleted();
            }
        });
    }
}
