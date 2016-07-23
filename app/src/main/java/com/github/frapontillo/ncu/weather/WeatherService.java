package com.github.frapontillo.ncu.weather;

import com.github.frapontillo.ncu.weather.openweather.WeatherForecast;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WeatherService {

    @GET("forecast/daily")
    Observable<WeatherForecast> getDailyForecast(@Query("q") String query,
                                                 @Query("mode") String mode,
                                                 @Query("units") String units,
                                                 @Query("cnt") int days);

}
