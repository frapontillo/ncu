package com.github.frapontillo.ncu.weather.service;

import android.content.Context;

import com.github.frapontillo.ncu.weather.model.Event;
import com.github.frapontillo.ncu.weather.model.Weather;
import com.jakewharton.rxrelay.BehaviorRelay;

import rx.Notification;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

public class WeatherServicePersisted implements WeatherService {

    private final WeatherRemoteDataSource weatherRemoteDataSource;
    private final WeatherLocalDataSource weatherLocalDataSource;

    private final BehaviorRelay<Event<Weather>> weatherRelay;

    public static WeatherServicePersisted newInstance(Context context) {
        return new WeatherServicePersisted(
                WeatherRemoteDataSource.newInstance(),
                WeatherLocalDataSource.newInstance(context)
        );
    }

    public WeatherServicePersisted(WeatherRemoteDataSource weatherRemoteDataSource,
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
                .getLatestWeatherFor(zipCode)
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
                .getWeekWeather(zipCode)
                .map(weatherLocalDataSource.persistWeather());
    }

    private Observable.Transformer<Weather, Event<Weather>> asEvent() {
        return new Observable.Transformer<Weather, Event<Weather>>() {
            @Override
            public Observable<Event<Weather>> call(Observable<Weather> weatherObservable) {
                return weatherObservable
                        .materialize()
                        .map(notificationToEvent())
                        .startWith(Event.<Weather>idle());
            }
        };
    }

    private Func1<Notification<Weather>, Event<Weather>> notificationToEvent() {
        return new Func1<Notification<Weather>, Event<Weather>>() {
            @Override
            public Event<Weather> call(Notification<Weather> weatherNotification) {
                if (weatherNotification.isOnNext()) {
                    return Event.loading(weatherNotification.getValue());
                } else if (weatherNotification.isOnError()) {
                    return Event.error(weatherNotification.getThrowable());
                } else if (weatherNotification.isOnCompleted()) {
                    return Event.idle();
                }
                throw new IllegalStateException("Notification is not onCompleted nor onError nor onNext.");
            }
        };
    }

}
