package com.github.frapontillo.ncu.data.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.github.frapontillo.ncu.data.UnsupportedUriOperationException;
import com.github.frapontillo.ncu.data.WeatherDbHelper;
import com.github.frapontillo.ncu.data.contract.WeatherContract;

public class WeatherFromLocationAndDatePartialProvider extends PartialProvider<WeatherDbHelper> {
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
                WeatherContract.LOCATION_AND_DATE_SELECTION_QUERY_PART,
                new String[]{location, String.valueOf(date)},
                null,
                null,
                sortOrder
        );
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedUriOperationException("insert", uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedUriOperationException("delete", uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedUriOperationException("update", uri);
    }
}
