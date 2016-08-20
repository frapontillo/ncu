package com.github.frapontillo.ncu.weather.service;

import com.github.frapontillo.ncu.weather.model.Event;
import com.github.frapontillo.ncu.weather.model.Weather;

import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

class Helper {

    static Observable<Void> asObservable(final Action0 action0) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                action0.call();
                subscriber.onCompleted();
            }
        });
    }

    static Observable.Transformer<Weather, Event<Weather>> asEvent() {
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

    private static Func1<Notification<Weather>, Event<Weather>> notificationToEvent() {
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
