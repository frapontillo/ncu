package com.github.frapontillo.ncu.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.frapontillo.ncu.R;
import com.github.frapontillo.ncu.detail.DetailActivity;
import com.github.frapontillo.ncu.settings.SettingsActivity;
import com.github.frapontillo.ncu.settings.SettingsHelper;
import com.github.frapontillo.ncu.weather.openweather.WeatherData;
import com.github.frapontillo.ncu.weather.openweather.WeatherFetcher;

public class MainFragment extends Fragment {
    private WeatherDataAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container);

        ListView forecastList = (ListView) rootView.findViewById(R.id.forecast_list);
        listAdapter = new WeatherDataAdapter(getActivity(), R.layout.list_item_forecast);
        forecastList.setAdapter(listAdapter);
        forecastList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), DetailActivity.class).putExtra(DetailActivity.EXTRA_WEATHER_DATA,
                                                                                       listAdapter.getItem(position)));
            }
        });
        fetchWeather();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // notify change to the adapter so that it can update the list temperature units
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.forecast_menu_refresh) {
            fetchWeather();
            return true;
        }
        if (itemId == R.id.forecast_menu_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        if (itemId == R.id.forecast_menu_location) {
            Intent mapsIntent = new Intent(Intent.ACTION_VIEW);
            Uri uri = new Uri.Builder().scheme("geo").path("0:0").appendQueryParameter("q", SettingsHelper.getZipCode(getContext())).build();
            mapsIntent.setData(uri);
            if (mapsIntent.resolveActivity(getActivity().getPackageManager()) == null) {
                Toast.makeText(getActivity(), R.string.no_maps_installed, Toast.LENGTH_LONG).show();
            } else {
                startActivity(mapsIntent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchWeather() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        new FetchWeatherTask().execute(SettingsHelper.getZipCode(getActivity()));

    }

    private class FetchWeatherTask extends AsyncTask<String, Void, WeatherData> {

        private final WeatherFetcher weatherFetcher = new WeatherFetcher(getActivity());

        @Override
        protected WeatherData doInBackground(String... params) {
            return weatherFetcher.getWeatherInfo(params[0]);
        }

        @Override
        protected void onPostExecute(WeatherData data) {
            super.onPostExecute(data);
            listAdapter.clear();
            listAdapter.addAll(data.weatherDays());
        }
    }

}
