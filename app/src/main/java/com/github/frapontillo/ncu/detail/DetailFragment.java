package com.github.frapontillo.ncu.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.frapontillo.ncu.R;
import com.github.frapontillo.ncu.settings.SettingsHelper;
import com.github.frapontillo.ncu.weather.openweather.WeatherDay;

public class DetailFragment extends Fragment {
    private TextView label;
    private boolean asImperial;
    private WeatherDay data;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        asImperial = SettingsHelper.isImperial(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        label = (TextView) inflater.inflate(R.layout.list_item_forecast, container);
        return label;
    }

    @Override
    public void onResume() {
        super.onResume();
        data = getActivity().getIntent().getExtras().getParcelable(DetailActivity.EXTRA_WEATHER_DATA);
        if (data != null) {
            label.setText(String.format(getString(R.string.weather_data_template),
                                        data.day(), data.description(), data.high(asImperial), data.low(asImperial)
            ));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail, menu);

        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.detail_menu_share));

        boolean imperial = SettingsHelper.isImperial(getActivity());
        String shareString = String.format(
                getString(R.string.weather_data_share_template), data.day(), data.description(), data.high(imperial), data.low(imperial));
        shareActionProvider.setShareIntent(ShareCompat.IntentBuilder.from(getActivity())
                                                   .setText(shareString)
                                                   .setType("text/plain")
                                                   .getIntent());
    }
}
