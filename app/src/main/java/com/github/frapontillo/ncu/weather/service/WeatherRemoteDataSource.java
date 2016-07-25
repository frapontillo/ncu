package com.github.frapontillo.ncu.weather.service;

import com.github.frapontillo.ncu.weather.api.ApiWeatherCity;
import com.github.frapontillo.ncu.weather.api.ApiWeatherForecast;
import com.github.frapontillo.ncu.weather.api.ApiWeatherForecastDay;
import com.github.frapontillo.ncu.weather.api.ApiWeatherForecastDayCondition;
import com.github.frapontillo.ncu.weather.api.ApiWeatherService;
import com.github.frapontillo.ncu.weather.api.OpenWeatherMapApiKeyInterceptor;
import com.github.frapontillo.ncu.weather.model.Weather;
import com.github.frapontillo.ncu.weather.model.WeatherDay;
import com.github.frapontillo.ncu.weather.model.WeatherLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryanharter.auto.value.gson.AutoValueGsonTypeAdapterFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

public class WeatherRemoteDataSource {

    private static final int NO_WEATHER_ID = -1;
    private static final String OPEN_WEATHER_MAP_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String NO_WEATHER_DESCRIPTION = "Who knows?";

    private final ApiWeatherService apiWeatherService;

    public static WeatherRemoteDataSource newInstance() {
        CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new AutoValueGsonTypeAdapterFactory())
                .create();
        Converter.Factory gsonConverterFactory = GsonConverterFactory.create(gson);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new OpenWeatherMapApiKeyInterceptor())
                .build();

        ApiWeatherService apiWeatherService = new Retrofit.Builder()
                .baseUrl(OPEN_WEATHER_MAP_BASE_URL)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build()
                .create(ApiWeatherService.class);

        return new WeatherRemoteDataSource(apiWeatherService);
    }

    public WeatherRemoteDataSource(ApiWeatherService apiWeatherService) {
        this.apiWeatherService = apiWeatherService;
    }

    public Observable<Weather> getWeekWeather(String zipCode) {
        return apiWeatherService
                .getDailyForecast(zipCode, "json", "metric", 7)
                .map(toDomainModel(zipCode));
    }

    private Func1<? super ApiWeatherForecast, ? extends Weather> toDomainModel(final String zipCode) {
        return new Func1<ApiWeatherForecast, Weather>() {
            @Override
            public Weather call(ApiWeatherForecast apiWeatherForecast) {
                return Weather.builder()
                        .location(toWeatherLocation(zipCode, apiWeatherForecast.city()))
                        .days(toWeatherDays(apiWeatherForecast.days()))
                        .build();
            }
        };
    }

    private WeatherLocation toWeatherLocation(String zipCode, ApiWeatherCity city) {
        return WeatherLocation.builder()
                .zipCode(zipCode)
                .cityName(city.name())
                .latitude(city.coordinates().latitude())
                .longitude(city.coordinates().longitude())
                .build();
    }

    private List<WeatherDay> toWeatherDays(List<ApiWeatherForecastDay> apiDays) {
        List<WeatherDay> days = new ArrayList<>(apiDays.size());
        for (ApiWeatherForecastDay apiDay : apiDays) {
            days.add(toWeatherDay(apiDay));
        }
        return days;
    }

    private WeatherDay toWeatherDay(ApiWeatherForecastDay apiDay) {
        return WeatherDay.builder()
                .weatherId(getFirstWeatherIdOrMinusOne(apiDay))
                .weatherDescription(getFirstWeatherDescription(apiDay))
                .date(getDate(apiDay))
                .humidity(apiDay.humidity())
                .pressure(apiDay.pressure())
                .maxTemperature(apiDay.temperatures().max())
                .minTemperature(apiDay.temperatures().min())
                .windDirection(apiDay.windDirection())
                .windSpeed(apiDay.windSpeed())
                .build();
    }

    private int getFirstWeatherIdOrMinusOne(ApiWeatherForecastDay apiDay) {
        ApiWeatherForecastDayCondition condition = getFirstWeatherDayConditionOrNull(apiDay);
        if (condition == null) {
            return NO_WEATHER_ID;
        }
        return condition.id();
    }

    private String getFirstWeatherDescription(ApiWeatherForecastDay apiDay) {
        ApiWeatherForecastDayCondition condition = getFirstWeatherDayConditionOrNull(apiDay);
        if (condition == null) {
            return NO_WEATHER_DESCRIPTION;
        }
        return condition.description();
    }

    private ApiWeatherForecastDayCondition getFirstWeatherDayConditionOrNull(ApiWeatherForecastDay apiDay) {
        List<ApiWeatherForecastDayCondition> conditions = apiDay.conditions();
        if (conditions == null || conditions.size() == 0) {
            return null;
        }
        return conditions.get(0);
    }

    private Date getDate(ApiWeatherForecastDay apiDay) {
        return new Date(apiDay.date() * 1000);
    }

}
