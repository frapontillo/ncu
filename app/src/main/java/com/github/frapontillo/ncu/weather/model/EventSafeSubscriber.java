package com.github.frapontillo.ncu.weather.model;

import rx.Subscriber;
import rx.functions.Action1;
import rx.observers.SafeSubscriber;

public class EventSafeSubscriber<T> extends SafeSubscriber<Event<T>> {

    public EventSafeSubscriber(final Action1<Event<T>> onNext) {
        super(new Subscriber<Event<T>>() {
            @Override
            public void onCompleted() {
                throw new IllegalStateException("Events should never complete.");
            }

            @Override
            public void onError(Throwable e) {
                throw new IllegalStateException("Events should never fail.");
            }

            @Override
            public void onNext(Event<T> event) {
                onNext.call(event);
            }
        });
    }

}
