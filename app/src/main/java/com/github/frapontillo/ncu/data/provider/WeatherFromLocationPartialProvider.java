package com.github.frapontillo.ncu.data.provider;

import android.database.Cursor;
import android.net.Uri;

import com.github.frapontillo.ncu.data.WeatherDbHelper;
import com.github.frapontillo.ncu.data.contract.LocationContract;
import com.github.frapontillo.ncu.data.contract.WeatherContract;

public class WeatherFromLocationPartialProvider extends QueryOnlyPartialProvider<WeatherDbHelper> {

    private static final String LIMIT_TO_DAYS_IN_WEEK = "7";

    private final WeatherContract weatherContract;

    public WeatherFromLocationPartialProvider(WeatherDbHelper helper) {
        super(helper);
        weatherContract = new WeatherContract();
    }

    @Override
    public String getType() {
        return WeatherContract.CONTENT_DIR_TYPE;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String location = weatherContract.getLocation(uri);
        return WeatherContract.WEATHER_JOIN_LOCATION_QUERY_BUILDER.query(
                getHelper().getReadableDatabase(),
                projection,
                LocationContract.LOCATION_SELECTION_SETTING_PART,
                new String[]{location},
                null,
                null,
                sortOrder,
                LIMIT_TO_DAYS_IN_WEEK
        );
    }

}
