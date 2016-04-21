package com.github.frapontillo.ncu.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

import com.github.frapontillo.ncu.R;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences preferences;
    private String PREF_ZIP_CODE;
    private String PREF_USE_IMPERIAL;
    private CheckBoxPreference imperialPreference;
    private EditTextPreference zipCodePreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PREF_ZIP_CODE = getResources().getString(R.string.pref_zip_code);
        PREF_USE_IMPERIAL = getResources().getString(R.string.pref_use_imperial);

        preferences = getPreferenceManager().getSharedPreferences();
        addPreferencesFromResource(R.xml.settings);

        // change the summary for each preference
        zipCodePreference = (EditTextPreference) getPreferenceManager().findPreference(PREF_ZIP_CODE);
        imperialPreference = (CheckBoxPreference) getPreferenceManager().findPreference(PREF_USE_IMPERIAL);
        handleChangeZipCode(zipCodePreference.getText());
        handleChangeUseImperial(imperialPreference.isChecked());
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    private boolean handleChangeUseImperial(boolean useImperial) {
        imperialPreference.setSummary(useImperial ? R.string.use_metric_summary : R.string.use_imperial_summary);
        return true;
    }

    private boolean handleChangeZipCode(String newValue) {
        zipCodePreference.setSummary(newValue);
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREF_ZIP_CODE)) {
            handleChangeZipCode(sharedPreferences.getString(key, null));
        }
        if (key.equals(PREF_USE_IMPERIAL)) {
            handleChangeUseImperial(sharedPreferences.getBoolean(key, false));
        }
    }
}
