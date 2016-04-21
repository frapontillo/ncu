package com.github.frapontillo.ncu.settings;

import android.content.Context;
import android.preference.PreferenceManager;

import com.github.frapontillo.ncu.R;

public class SettingsHelper {
    public static boolean isImperial(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.pref_use_imperial), false);
    }

    public static String getZipCode(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_zip_code), null);
    }
}
