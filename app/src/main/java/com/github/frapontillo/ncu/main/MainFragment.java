package com.github.frapontillo.ncu.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.github.frapontillo.ncu.SunshineApp;
import com.github.frapontillo.ncu.detail.DetailActivity;
import com.github.frapontillo.ncu.settings.SettingsActivity;
import com.github.frapontillo.ncu.settings.SettingsHelper;
import com.github.frapontillo.ncu.weather.model.Event;
import com.github.frapontillo.ncu.weather.service.WeatherService;
import com.github.frapontillo.ncu.weather.model.Weather;

import rx.Subscriber;
import rx.Subscription;
import rx.observers.SafeSubscriber;

public class MainFragment extends Fragment {
    private WeatherDataAdapter listAdapter;
    private Subscription subscription;

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
                startActivity(new Intent(getActivity(), DetailActivity.class).putExtra(
                        DetailActivity.EXTRA_WEATHER_DATA,
                        listAdapter.getItem(position)
                ));
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        SafeSubscriber<Event<Weather>> subscriber = new SafeSubscriber<>(new Subscriber<Event<Weather>>() {
            @Override
            public void onCompleted() {
                throw new IllegalStateException("Events should never complete.");
            }

            @Override
            public void onError(Throwable e) {
                throw new IllegalStateException("Events should never fail.");
            }

            @Override
            public void onNext(Event<Weather> weatherEvent) {
                if (weatherEvent.state().equals(Event.State.LOADING)) {
                    listAdapter.clear();
                    listAdapter.addAll(weatherEvent.data().get().days());
                }
            }
        });

        subscription = getWeatherService()
                .getWeather(getZipCode())
                .subscribe(subscriber);

        // notify change to the adapter so that it can update the list temperature units
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        subscription.unsubscribe();
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
            Uri uri = new Uri.Builder().scheme("geo").path("0:0").appendQueryParameter("q", getZipCode()).build();
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

        getWeatherService().refreshWeather(getZipCode()).call();
    }

    private WeatherService getWeatherService() {
        return ((SunshineApp) getActivity().getApplication()).getWeatherService();
    }

    private String getZipCode() {
        return SettingsHelper.getZipCode(getActivity());
    }

}
