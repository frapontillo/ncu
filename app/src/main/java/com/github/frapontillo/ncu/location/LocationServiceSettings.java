package com.github.frapontillo.ncu.location;

import android.content.Context;

import com.github.frapontillo.ncu.settings.SettingsHelper;

public class LocationServiceSettings implements LocationService {

    private final Context context;

    public LocationServiceSettings(Context context) {
        this.context = context;
    }

    @Override
    public String getZipCode() {
        return SettingsHelper.getZipCode(context);
    }

}
