package com.github.frapontillo.ncu.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.frapontillo.ncu.R;
import com.github.frapontillo.ncu.settings.SettingsHelper;
import com.github.frapontillo.ncu.weather.WeatherData;

public class DetailFragment extends Fragment {
    private TextView label;
    private boolean asImperial;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        WeatherData data = getActivity().getIntent().getExtras().getParcelable(DetailActivity.EXTRA_WEATHER_DATA);
        if (data != null) {
            label.setText(String.format(getString(R.string.weather_data_template),
                                        data.day(), data.description(), data.high(asImperial), data.low(asImperial)));
        }
    }
}
