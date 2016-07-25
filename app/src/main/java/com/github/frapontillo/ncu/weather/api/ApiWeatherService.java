package com.github.frapontillo.ncu.weather.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiWeatherService {

    @GET("forecast/daily")
    Observable<ApiWeatherForecast> getDailyForecast(@Query("q") String query,
                                                    @Query("mode") String mode,
                                                    @Query("units") String units,
                                                    @Query("cnt") int days);

}
