package com.github.frapontillo.ncu.main;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.github.frapontillo.ncu.R;
import com.github.frapontillo.ncu.detail.DetailActivity;
import com.github.frapontillo.ncu.settings.SettingsActivity;
import com.github.frapontillo.ncu.weather.WeatherData;
import com.github.frapontillo.ncu.weather.WeatherFetcher;

public class MainFragment extends Fragment {
    private SharedPreferences preferences;
    private WeatherDataAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
        inflater.inflate(R.menu.forecast, menu);
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
        return super.onOptionsItemSelected(item);
    }

    private void fetchWeather() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        new FetchWeatherTask().execute(preferences.getString(getResources().getString(R.string.pref_zip_code), null));

    }

    private class FetchWeatherTask extends AsyncTask<String, Void, WeatherData[]> {

        private final WeatherFetcher weatherFetcher = new WeatherFetcher();

        @Override
        protected WeatherData[] doInBackground(String... params) {
            return weatherFetcher.getWeatherInfo(params[0]);
        }

        @Override
        protected void onPostExecute(WeatherData[] data) {
            super.onPostExecute(data);
            listAdapter.clear();
            listAdapter.addAll(data);
        }
    }

}
