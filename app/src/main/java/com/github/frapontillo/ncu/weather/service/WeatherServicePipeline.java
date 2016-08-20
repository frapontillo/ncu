package com.github.frapontillo.ncu.weather.service;

import android.content.Context;

import com.github.frapontillo.ncu.Clock;
import com.github.frapontillo.ncu.SystemClock;
import com.github.frapontillo.ncu.weather.model.Event;
import com.github.frapontillo.ncu.weather.model.Weather;
import com.github.frapontillo.ncu.weather.model.WeatherDay;
import com.jakewharton.rxrelay.BehaviorRelay;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

import static com.github.frapontillo.ncu.weather.service.WeatherServiceAsync.asEvent;
import static java.util.Arrays.asList;

public class WeatherServicePipeline implements WeatherService {

    private final BehaviorRelay<Event<Weather>> weatherRelay;
    private final Clock clock;
    private final List<WeatherReadableDataSource> weatherReadableDataSources;
    private final List<WeatherWritableDataSource> writableDataSources;

    public static WeatherServicePipeline newInstance(Context context) {
        WeatherLocalDataSource weatherLocalDataSource = WeatherLocalDataSource.newInstance(context);
        WeatherRemoteDataSource weatherRemoteDataSource = WeatherRemoteDataSource.newInstance();

        List<WeatherReadableDataSource> readableDataSources = asList(weatherLocalDataSource, weatherRemoteDataSource);
        List<WeatherWritableDataSource> writableDataSources = Collections.<WeatherWritableDataSource>singletonList(weatherLocalDataSource);

        return new WeatherServicePipeline(
                new SystemClock(),
                readableDataSources,
                writableDataSources
        );
    }

    private WeatherServicePipeline(Clock clock,
                                   List<WeatherReadableDataSource> readableDataSources,
                                   List<WeatherWritableDataSource> writableDataSources) {

        this.clock = clock;
        this.weatherReadableDataSources = readableDataSources;
        this.writableDataSources = writableDataSources;

        this.weatherRelay = BehaviorRelay.create(Event.<Weather>idle());
    }

    @Override
    public Observable<Event<Weather>> getWeather(String zipCode) {
        return weatherRelay.asObservable()
                .startWith(initialiseFromDataSources(zipCode))
                .distinctUntilChanged();
    }

    private Observable<Event<Weather>> initialiseFromDataSources(final String zipCode) {
        return Observable.from(weatherReadableDataSources)
                .flatMap(getWeatherFromDataSource(zipCode))
                .takeUntil(dataIsNotOld())
                // TODO: persist here only if data comes from a fresh source
                .compose(asEvent())
                .doOnNext(weatherRelay);

    }

    private Func1<Weather, Boolean> dataIsNotOld() {
        return new Func1<Weather, Boolean>() {
            @Override
            public Boolean call(Weather weather) {
                WeatherDay oldestWeatherDay = weather.days().get(0);
                Date oldestWeatherDate = oldestWeatherDay.date();
                return oldestWeatherDate.before(clock.getTodayAtMidnight());
            }
        };
    }

    @Override
    public Action0 refreshWeather(final String zipCode) {
        return new Action0() {
            @Override
            public void call() {
                updateAndWriteWeather(zipCode)
                        .compose(asEvent())
                        .subscribe(weatherRelay);
            }
        };
    }

    private Observable<Weather> updateAndWriteWeather(String zipCode) {
        return Observable.from(weatherReadableDataSources)
                .first(thatRetrievesFreshData())
                .flatMap(getWeatherFromDataSource(zipCode))
                .map(persistInAllWritableDataSources());
    }

    private Func1<WeatherReadableDataSource, Boolean> thatRetrievesFreshData() {
        return new Func1<WeatherReadableDataSource, Boolean>() {
            @Override
            public Boolean call(WeatherReadableDataSource weatherReadableDataSource) {
                return weatherReadableDataSource.retrievesFreshData();
            }
        };
    }

    private Func1<WeatherReadableDataSource, Observable<Weather>> getWeatherFromDataSource(final String zipCode) {
        return new Func1<WeatherReadableDataSource, Observable<Weather>>() {
            @Override
            public Observable<Weather> call(WeatherReadableDataSource weatherReadableDataSource) {
                return weatherReadableDataSource.getWeather(zipCode);
            }
        };
    }

    private Func1<? super Weather, ? extends Weather> persistInAllWritableDataSources() {
        return new Func1<Weather, Weather>() {
            @Override
            public Weather call(final Weather weather) {
                return Observable.from(writableDataSources)
                        .map(persistInWritableDataSource(weather))
                        .toBlocking()
                        .last();
            }
        };
    }

    private Func1<WeatherWritableDataSource, Weather> persistInWritableDataSource(final Weather weather) {
        return new Func1<WeatherWritableDataSource, Weather>() {
            @Override
            public Weather call(WeatherWritableDataSource weatherWritableDataSource) {
                return weatherWritableDataSource.persistWeather(weather);
            }
        };
    }

}
