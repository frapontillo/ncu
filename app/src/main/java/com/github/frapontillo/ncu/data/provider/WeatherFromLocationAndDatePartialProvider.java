package com.github.frapontillo.ncu.data.provider;

import android.database.Cursor;
import android.net.Uri;

import com.github.frapontillo.ncu.data.WeatherDbHelper;
import com.github.frapontillo.ncu.data.contract.WeatherContract;

public class WeatherFromLocationAndDatePartialProvider extends QueryOnlyPartialProvider<WeatherDbHelper> {
    private final WeatherContract weatherContract;

    public WeatherFromLocationAndDatePartialProvider(WeatherDbHelper helper) {
        super(helper);
        weatherContract = new WeatherContract();
    }

    @Override
    public String getType() {
        return WeatherContract.CONTENT_ITEM_TYPE;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String location = weatherContract.getLocation(uri);
        long date = weatherContract.getDate(uri);

        return WeatherContract.WEATHER_JOIN_LOCATION_QUERY_BUILDER.query(
                getHelper().getReadableDatabase(),
                projection,
                WeatherContract.SELECTION_LOCATION_SETTING_EQUALS_AND_WEATHER_DATE_GREATER_OR_EQUALS,
                new String[]{location, String.valueOf(date)},
                null,
                null,
                sortOrder
        );
    }

}
