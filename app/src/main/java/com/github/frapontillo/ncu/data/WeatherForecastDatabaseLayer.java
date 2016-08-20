package com.github.frapontillo.ncu.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.github.frapontillo.ncu.data.contract.LocationContract;
import com.github.frapontillo.ncu.data.contract.WeatherContract;
import com.github.frapontillo.ncu.weather.model.Weather;
import com.github.frapontillo.ncu.weather.model.WeatherDay;
import com.github.frapontillo.ncu.weather.model.WeatherLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.github.frapontillo.ncu.data.provider.CursorHelper.*;

public class WeatherForecastDatabaseLayer {

    private final ContentResolver contentResolver;
    private final WeatherContract weatherContract;

    public WeatherForecastDatabaseLayer(ContentResolver contentResolver, WeatherContract weatherContract) {
        this.contentResolver = contentResolver;
        this.weatherContract = weatherContract;
    }

    public Weather persistWeatherForecast(Weather weather, long locationId) {
        if (weather != null) {
            List<WeatherDay> weatherDays = weather.days();
            ContentValues[] daysValues = weatherContract.toContentValues(weatherDays, locationId);
            contentResolver.bulkInsert(WeatherContract.CONTENT_URI, daysValues);
        }

        return weather;
    }

    public Weather getLatestWeatherFor(String zipCode, Date date) {
        Cursor cursor = contentResolver.query(
                WeatherContract.CONTENT_URI.buildUpon().appendPath(zipCode).build(),
                null,
                WeatherContract.SELECTION_WEATHER_DATE_AFTER,
                new String[]{Long.toString(date.getTime())},
                WeatherContract.COLUMNS.DATE
        );

        Weather weather = null;

        if (cursor != null && cursor.moveToFirst()) {
            try {
                weather = buildWeather(cursor);
            } finally {
                cursor.close();
            }
        }

        return weather;
    }

    private Weather buildWeather(Cursor cursor) {
        return Weather.builder()
                .location(buildWeatherLocation(cursor))
                .days(buildWeatherDays(cursor))
                .build();
    }

    private WeatherLocation buildWeatherLocation(Cursor cursor) {
        cursor.moveToFirst();

        return WeatherLocation.builder()
                .zipCode(getString(cursor, LocationContract.COLUMNS.SETTING))
                .cityName(getString(cursor, LocationContract.COLUMNS.CITY_NAME))
                .latitude(getDouble(cursor, LocationContract.COLUMNS.LATITUDE))
                .longitude(getDouble(cursor, LocationContract.COLUMNS.LONGITUDE))
                .build();
    }

    private List<WeatherDay> buildWeatherDays(Cursor cursor) {
        int numberOfDays = cursor.getCount();
        List<WeatherDay> weatherDays = new ArrayList<>(numberOfDays);

        cursor.moveToFirst();

        do {
            WeatherDay weatherDay = buildWeatherDay(cursor);
            weatherDays.add(weatherDay);
        } while (cursor.moveToNext());

        return weatherDays;
    }

    private WeatherDay buildWeatherDay(Cursor cursor) {
        return WeatherDay.builder()
                .weatherId(getInt(cursor, WeatherContract.COLUMNS.WEATHER_ID))
                .weatherDescription(getString(cursor, WeatherContract.COLUMNS.WEATHER_DESCRIPTION))
                .date(getDate(cursor, WeatherContract.COLUMNS.DATE))
                .humidity(getDouble(cursor, WeatherContract.COLUMNS.HUMIDITY))
                .pressure(getDouble(cursor, WeatherContract.COLUMNS.PRESSURE))
                .maxTemperature((getDouble(cursor, WeatherContract.COLUMNS.TEMP_MAX)))
                .minTemperature((getDouble(cursor, WeatherContract.COLUMNS.TEMP_MIN)))
                .windSpeed(getDouble(cursor, WeatherContract.COLUMNS.WIND_SPEED))
                .windDirection(getDouble(cursor, WeatherContract.COLUMNS.WIND_SPEED))
                .build();
    }

}
