package com.github.frapontillo.ncu.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.frapontillo.ncu.R;
import com.github.frapontillo.ncu.settings.SettingsHelper;
import com.github.frapontillo.ncu.weather.model.WeatherDay;
import com.github.frapontillo.ncu.weather.model.WeatherDayFormatter;

public class WeatherDataAdapter extends ArrayAdapter<WeatherDay> {
    private final LayoutInflater layoutInflater;

    public WeatherDataAdapter(Context context, int resource) {
        super(context, resource);
        layoutInflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherDay weatherDay = getItem(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_forecast, parent, false);
            // this is kinda useless right now, convertView is the only element in the ViewHolder
            convertView.setTag(new ViewHolder((TextView) convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        boolean isImperial = SettingsHelper.isImperial(getContext());
        WeatherDayFormatter weatherDayFormatter = new WeatherDayFormatter(weatherDay);
        viewHolder.text.setText(
                String.format(
                        getContext().getString(R.string.weather_data_template),
                        weatherDayFormatter.getDay(),
                        weatherDay.weatherDescription(),
                        weatherDayFormatter.getMaxTemperature(isImperial),
                        weatherDayFormatter.getMinTemperature(isImperial)
                )
        );
        return convertView;
    }

    public class ViewHolder {
        TextView text;

        public ViewHolder(TextView text) {
            this.text = text;
        }
    }
}
