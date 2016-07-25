package com.github.frapontillo.ncu.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.github.frapontillo.ncu.data.contract.LocationContract;
import com.github.frapontillo.ncu.weather.model.WeatherLocation;

public class WeatherLocationDatabaseLayer {

    private final ContentResolver contentResolver;
    private final LocationContract locationContract;

    public WeatherLocationDatabaseLayer(ContentResolver contentResolver, LocationContract locationContract) {
        this.contentResolver = contentResolver;
        this.locationContract = locationContract;
    }

    public long saveLocationAndGetId(WeatherLocation weatherLocation) {
        Cursor existingLocation = queryLocationBySettingEqualsZipCode(weatherLocation);

        boolean wasFound = locationExists(existingLocation);

        long id;
        if (wasFound) {
            id = getLocationId(existingLocation);
        } else {
            id = insertNewLocationAndGetId(weatherLocation);
        }

        closeCursor(existingLocation);

        return id;
    }

    private boolean locationExists(Cursor existingLocation) {
        return existingLocation != null && existingLocation.moveToFirst();
    }

    private Cursor queryLocationBySettingEqualsZipCode(WeatherLocation weatherLocation) {
        return contentResolver.query(
                LocationContract.CONTENT_URI,
                new String[]{LocationContract._ID},
                LocationContract.LOCATION_SELECTION_SETTING_PART,
                new String[]{weatherLocation.zipCode()},
                null
        );
    }

    private long insertNewLocationAndGetId(WeatherLocation weatherLocation) {
        long id;ContentValues values = locationContract.toContentValues(weatherLocation);
        Uri insertedUri = contentResolver.insert(LocationContract.CONTENT_URI, values);
        id = locationContract.getId(insertedUri);
        return id;
    }

    private long getLocationId(Cursor existingLocation) {
        return existingLocation.getLong(existingLocation.getColumnIndex(LocationContract._ID));
    }

    private void closeCursor(Cursor existingLocation) {
        if (existingLocation != null) {
            existingLocation.close();
        }
    }

}
