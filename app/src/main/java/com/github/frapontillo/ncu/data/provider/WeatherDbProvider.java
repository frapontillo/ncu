package com.github.frapontillo.ncu.data.provider;

import com.github.frapontillo.ncu.data.WeatherDbHelper;
import com.github.frapontillo.ncu.data.contract.Contract;
import com.github.frapontillo.ncu.data.contract.LocationContract;
import com.github.frapontillo.ncu.data.contract.WeatherContract;

public class WeatherDbProvider extends MultipleContentProvider {

    @Override
    public boolean onCreate() {
        super.onCreate();
        WeatherDbHelper weatherDbHelper = new WeatherDbHelper(getContext());
        addPartialContentProvider(Contract.CONTENT_AUTHORITY, WeatherContract.TABLE_NAME, new WeatherPartialProvider(weatherDbHelper));
        addPartialContentProvider(Contract.CONTENT_AUTHORITY, WeatherContract.TABLE_NAME + "/*",
                                  new WeatherFromLocationPartialProvider(weatherDbHelper));
        addPartialContentProvider(Contract.CONTENT_AUTHORITY, WeatherContract.TABLE_NAME + "/*/#",
                                  new WeatherFromLocationAndDatePartialProvider(weatherDbHelper));
        addPartialContentProvider(Contract.CONTENT_AUTHORITY, LocationContract.TABLE_NAME, new LocationPartialProvider(weatherDbHelper));
        return true;
    }
}
