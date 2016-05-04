package com.github.frapontillo.ncu.data.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.github.frapontillo.ncu.data.WeatherDbHelper;
import com.github.frapontillo.ncu.data.contract.WeatherContract;

public class WeatherPartialProvider extends PartialProvider<WeatherDbHelper> {
    private final WeatherContract weatherContract;

    public WeatherPartialProvider(WeatherDbHelper helper) {
        super(helper);
        weatherContract = new WeatherContract();
    }

    @Override
    public String getType() {
        return WeatherContract.CONTENT_DIR_TYPE;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return getHelper().getReadableDatabase().query(WeatherContract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = getHelper().getWritableDatabase().insert(WeatherContract.TABLE_NAME, null, values);
        return weatherContract.buildItemUri(id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return getHelper().getWritableDatabase().delete(WeatherContract.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return getHelper().getWritableDatabase().update(WeatherContract.TABLE_NAME, values, selection, selectionArgs);
    }
}
