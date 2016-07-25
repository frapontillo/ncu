package com.github.frapontillo.ncu.navigator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.github.frapontillo.ncu.R;
import com.github.frapontillo.ncu.detail.DetailActivity;
import com.github.frapontillo.ncu.settings.SettingsActivity;
import com.github.frapontillo.ncu.weather.model.WeatherDay;

public class AndroidNavigator implements Navigator {

    private final Activity activity;

    public AndroidNavigator(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void toWeatherDay(WeatherDay weatherDay) {
        activity.startActivity(createWeatherDayActivity(weatherDay));
    }

    private Intent createWeatherDayActivity(WeatherDay weatherDay) {
        return new Intent(activity, DetailActivity.class).putExtra(DetailActivity.EXTRA_WEATHER_DATA, weatherDay);
    }

    @Override
    public void toSettings() {
        activity.startActivity(new Intent(activity, SettingsActivity.class));
    }

    @Override
    public void openMaps(String zipCode) {
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri = new Uri.Builder().scheme("geo").path("0:0").appendQueryParameter("q", zipCode).build();
        mapsIntent.setData(uri);
        if (mapsIntent.resolveActivity(activity.getPackageManager()) == null) {
            Toast.makeText(activity, R.string.no_maps_installed, Toast.LENGTH_LONG).show();
        } else {
            activity.startActivity(mapsIntent);
        }
    }

}
