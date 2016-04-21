package com.github.frapontillo.ncu;

import android.app.Application;
import android.preference.PreferenceManager;

public class SunshineApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }
}
