package com.github.frapontillo.ncu.main;

import com.github.frapontillo.ncu.location.LocationService;
import com.github.frapontillo.ncu.navigator.Navigator;
import com.github.frapontillo.ncu.weather.model.Event;
import com.github.frapontillo.ncu.weather.model.EventSafeSubscriber;
import com.github.frapontillo.ncu.weather.model.Weather;
import com.github.frapontillo.ncu.weather.model.WeatherDay;
import com.github.frapontillo.ncu.weather.service.WeatherService;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class WeatherPresenter {

    private final WeatherService weatherService;
    private final LocationService locationService;
    private final WeatherDisplayer weatherDisplayer;
    private final Navigator navigator;

    private CompositeSubscription subscriptions;

    public WeatherPresenter(WeatherService weatherService,
                            LocationService locationService,
                            WeatherDisplayer weatherDisplayer,
                            Navigator navigator) {

        this.weatherService = weatherService;
        this.locationService = locationService;
        this.weatherDisplayer = weatherDisplayer;
        this.navigator = navigator;
        this.subscriptions = new CompositeSubscription();
    }

    public void startPresenting() {
        weatherDisplayer.attach(weatherActionListener);

        subscriptions.add(
                weatherService
                        .getWeather(locationService.getZipCode())
                        .subscribe(subscriber)
        );
    }

    public void stopPresenting() {
        weatherDisplayer.detach(weatherActionListener);

        subscriptions.unsubscribe();
        subscriptions = new CompositeSubscription();
    }

    private WeatherActionListener weatherActionListener = new WeatherActionListener() {
        @Override
        public void selectDay(WeatherDay weatherDay) {
            navigator.toWeatherDay(weatherDay);
        }
    };

    private EventSafeSubscriber<Weather> subscriber = new EventSafeSubscriber<>(new Action1<Event<Weather>>() {
        @Override
        public void call(Event<Weather> event) {
            if (event.state().equals(Event.State.LOADING) && event.data().isPresent()) {
                weatherDisplayer.display(event.data().get());
            }
        }
    });

}
