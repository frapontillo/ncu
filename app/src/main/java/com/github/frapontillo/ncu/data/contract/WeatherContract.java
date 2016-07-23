package com.github.frapontillo.ncu.data.contract;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

import com.github.frapontillo.ncu.weather.openweather.WeatherForecastDay;
import com.github.frapontillo.ncu.weather.openweather.WeatherForecastDayCondition;

import java.util.Date;
import java.util.List;

public class WeatherContract implements BaseColumns {
    public static final String TABLE_NAME = "weather";
    public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Contract.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Contract.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    public static final Uri CONTENT_URI = Contract.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
    public static final SQLiteQueryBuilder WEATHER_JOIN_LOCATION_QUERY_BUILDER = new SQLiteQueryBuilder();
    public static final String WEATHER_SELECTION_QUERY_PART = WeatherContract.TABLE_NAME + "." + WeatherContract._ID + " = ?";
    public static final String LOCATION_AND_DATE_SELECTION_QUERY_PART = LocationContract.TABLE_NAME + "." + LocationContract.COLUMNS.SETTING + " = ?" +
            " AND " + WeatherContract.TABLE_NAME + "." + WeatherContract.COLUMNS.DATE + " >= ?";

    static {
        WEATHER_JOIN_LOCATION_QUERY_BUILDER.setTables(
                WeatherContract.TABLE_NAME + " INNER JOIN " + LocationContract.TABLE_NAME + " ON " +
                        WeatherContract.TABLE_NAME + "." + WeatherContract.COLUMNS.LOCATION_ID + " = " +
                        LocationContract.TABLE_NAME + "." + LocationContract._ID
        );
    }

    public static final class COLUMNS {
        public static final String LOCATION_ID = "location_id";
        public static final String DATE = "date";
        public static final String WEATHER_ID = "weather_id";
        public static final String TEMP_MAX = "temp_max";
        public static final String TEMP_MIN = "temp_min";
        public static final String HUMIDITY = "humidity";
        public static final String PRESSURE = "pressure";
        public static final String WIND_SPEED = "wind_speed";
        public static final String WIND_DIRECTION = "wind_direction";
    }

    public Uri buildItemUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public Uri buildWeatherWithLocationAndDateUri(String query, Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required.");
        }
        return buildWeatherWithLocationUri(query).buildUpon().appendPath(String.valueOf(date.getTime())).build();
    }

    public Uri buildWeatherWithLocationUri(String query) {
        if (query == null) {
            throw new IllegalArgumentException("Query is required.");
        }
        return CONTENT_URI.buildUpon().appendPath(query).build();
    }

    public long getId(Uri uri) {
        return Long.parseLong(uri.getPathSegments().get(1));
    }

    public String getLocation(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    public Long getDate(Uri uri) {
        return Long.parseLong(uri.getPathSegments().get(2));
    }

    public ContentValues[] toContentValues(List<WeatherForecastDay> data, long locationId) {
        ContentValues[] cvs = new ContentValues[data.size()];
        for (int i = 0; i < data.size(); i++) {
            cvs[i] = toContentValues(data.get(i), locationId);
        }
        return cvs;
    }

    public ContentValues toContentValues(WeatherForecastDay data, long locationId) {
        ContentValues cv = new ContentValues(9);
        cv.put(COLUMNS.DATE, data.date().getTime());
        cv.put(COLUMNS.WEATHER_ID, getFirstWeatherIdOrMinusOne(data));
        cv.put(COLUMNS.LOCATION_ID, locationId);
        cv.put(COLUMNS.HUMIDITY, data.humidity());
        cv.put(COLUMNS.PRESSURE, data.pressure());
        cv.put(COLUMNS.TEMP_MAX, data.temperatures().max());
        cv.put(COLUMNS.TEMP_MIN, data.temperatures().min());
        cv.put(COLUMNS.WIND_DIRECTION, data.windDirection());
        cv.put(COLUMNS.WIND_SPEED, data.windSpeed());
        return cv;
    }

    private int getFirstWeatherIdOrMinusOne(WeatherForecastDay data) {
        List<WeatherForecastDayCondition> conditions = data.conditions();
        if (conditions == null || conditions.isEmpty()) {
            return -1;
        }
        return conditions.get(0).id();
    }
}
