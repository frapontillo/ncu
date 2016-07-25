package com.github.frapontillo.ncu.weather.model;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Weather {

    public static Builder builder() {
        return new AutoValue_Weather.Builder();
    }

    public abstract WeatherLocation location();

    public abstract List<WeatherDay> days();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder location(WeatherLocation location);

        public abstract Builder days(List<WeatherDay> days);

        public abstract Weather build();

    }

}
