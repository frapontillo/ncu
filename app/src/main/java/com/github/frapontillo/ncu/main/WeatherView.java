package com.github.frapontillo.ncu.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.github.frapontillo.ncu.R;
import com.github.frapontillo.ncu.weather.model.Weather;

public class WeatherView extends FrameLayout implements WeatherDisplayer {

    private WeatherDataAdapter listAdapter;
    private ListView forecastList;

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.weather_view, this);

        forecastList = (ListView) findViewById(R.id.forecast_list);
        listAdapter = new WeatherDataAdapter(getContext(), R.layout.list_item_forecast);

        forecastList.setAdapter(listAdapter);
    }

    @Override
    public void display(Weather weather) {
        listAdapter.clear();
        // notify change to the adapter so that it can update the list temperature units
        listAdapter.notifyDataSetChanged();
        listAdapter.addAll(weather.days());
    }

    @Override
    public void attach(final WeatherActionListener weatherActionListener) {
        forecastList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (weatherActionListener != null) {
                    weatherActionListener.selectDay(listAdapter.getItem(position));
                }
            }
        });
    }

    @Override
    public void detach(WeatherActionListener weatherActionListener) {
        forecastList.setOnItemClickListener(null);
    }
}
