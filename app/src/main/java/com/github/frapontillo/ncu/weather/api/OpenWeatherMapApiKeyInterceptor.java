package com.github.frapontillo.ncu.weather.api;

import com.github.frapontillo.ncu.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OpenWeatherMapApiKeyInterceptor implements Interceptor {

    private static final String API_KEY = "appid";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        HttpUrl urlWithApiKey = request
                .url()
                .newBuilder()
                .addQueryParameter(API_KEY, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .build();

        Request newRequest = request
                .newBuilder()
                .url(urlWithApiKey)
                .build();

        return chain.proceed(newRequest);
    }

}
