package com.github.frapontillo.ncu.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.frapontillo.ncu.R;
import com.github.frapontillo.ncu.SunshineApp;
import com.github.frapontillo.ncu.location.LocationService;
import com.github.frapontillo.ncu.location.LocationServiceSettings;
import com.github.frapontillo.ncu.navigator.AndroidNavigator;
import com.github.frapontillo.ncu.navigator.Navigator;
import com.github.frapontillo.ncu.weather.service.WeatherService;

public class WeatherActivity extends AppCompatActivity {

    private WeatherService weatherService;
    private LocationService locationService;
    private Navigator navigator;
    private WeatherPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);

        weatherService = ((SunshineApp) getApplication()).getWeatherService();
        locationService = new LocationServiceSettings(this);
        WeatherDisplayer weatherDisplayer = (WeatherDisplayer) findViewById(R.id.weather_view);
        navigator = new AndroidNavigator(this);

        presenter = new WeatherPresenter(weatherService, locationService, weatherDisplayer, navigator);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stopPresenting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.forecast_menu_refresh) {
            fetchWeather();
            return true;
        }
        if (itemId == R.id.forecast_menu_settings) {
            navigator.toSettings();
            return true;
        }
        if (itemId == R.id.forecast_menu_location) {
            navigator.openMaps(locationService.getZipCode());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchWeather() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        weatherService.refreshWeather(locationService.getZipCode());
    }

}
