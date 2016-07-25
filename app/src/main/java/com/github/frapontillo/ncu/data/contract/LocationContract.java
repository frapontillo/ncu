package com.github.frapontillo.ncu.data.contract;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import com.github.frapontillo.ncu.weather.model.WeatherLocation;

public class LocationContract implements BaseColumns {
    public static final String TABLE_NAME = "location";
    public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Contract.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Contract.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    public static final Uri CONTENT_URI = Contract.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
    public static final String LOCATION_SELECTION_SETTING_PART = TABLE_NAME + "." + COLUMNS.SETTING + " = ?";

    public static final class COLUMNS {
        public static final String SETTING = "setting";
        public static final String CITY_NAME = "city_name";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
    }

    public Uri buildItemUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public long getId(Uri uri) {
        return Long.parseLong(uri.getPathSegments().get(1));
    }

    public ContentValues toContentValues(WeatherLocation weatherLocation) {
        ContentValues cv = new ContentValues(4);
        cv.put(COLUMNS.SETTING, weatherLocation.zipCode());
        cv.put(COLUMNS.CITY_NAME, weatherLocation.cityName());
        cv.put(COLUMNS.LATITUDE, weatherLocation.latitude());
        cv.put(COLUMNS.LONGITUDE, weatherLocation.longitude());
        return cv;
    }
}
