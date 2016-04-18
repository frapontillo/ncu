package com.github.frapontillo.ncu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container);

        List<String> forecast = Arrays.asList("Today - Sunny - 14/4", "Tomorrow - Cloudy - 12/1", "Wednesday - Partly cloudy - 12/3",
                                              "Thursday - Sunny - 18/11", "Friday - Cloudy - 15/9", "Saturday - Partly cloudy - 11/5",
                                              "Sunday - Snowy - 6/-4"
        );

        ListView forecastList = (ListView) rootView.findViewById(R.id.forecast_list);
        forecastList.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast, forecast));

        return rootView;
    }
}
