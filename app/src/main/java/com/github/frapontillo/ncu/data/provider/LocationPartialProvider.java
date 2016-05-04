package com.github.frapontillo.ncu.data.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.github.frapontillo.ncu.data.WeatherDbHelper;
import com.github.frapontillo.ncu.data.contract.LocationContract;

public class LocationPartialProvider extends PartialProvider<WeatherDbHelper> {
    private final LocationContract locationContract;

    public LocationPartialProvider(WeatherDbHelper helper) {
        super(helper);
        locationContract = new LocationContract();
    }

    @Override
    public String getType() {
        return LocationContract.CONTENT_DIR_TYPE;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return getHelper().getReadableDatabase().query(LocationContract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = getHelper().getWritableDatabase().insert(LocationContract.TABLE_NAME, null, values);
        return locationContract.buildItemUri(id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return getHelper().getWritableDatabase().delete(LocationContract.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return getHelper().getWritableDatabase().update(LocationContract.TABLE_NAME, values, selection, selectionArgs);
    }
}
