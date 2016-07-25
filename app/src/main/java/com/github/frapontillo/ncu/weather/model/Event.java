package com.github.frapontillo.ncu.weather.model;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;

@AutoValue
public abstract class Event<T> {

    public abstract Optional<T> data();

    public abstract State state();

    public abstract Optional<Throwable> error();

    public static <T> Builder<T> builder() {
        return new AutoValue_Event.Builder<>();
    }

    public static <T> Event<T> idle() {
        return Event.<T>builder()
                .state(State.IDLE)
                .data(Optional.<T>absent())
                .build();
    }

    public static <T> Event<T> loading(T data) {
        return Event.<T>builder()
                .state(State.LOADING)
                .data(Optional.of(data))
                .build();
    }

    public static <T> Event<T> error(Throwable error) {
        return Event.<T>builder()
                .state(State.ERROR)
                .error(Optional.of(error))
                .build();
    }

    @AutoValue.Builder
    public static abstract class Builder<T> {

        public abstract Builder<T> data(Optional<T> data);

        public abstract Builder<T> state(State state);

        public abstract Builder<T> error(Optional<Throwable> error);

        public abstract Event<T> build();

    }

    public enum State {
        IDLE,
        LOADING,
        ERROR
    }

}
