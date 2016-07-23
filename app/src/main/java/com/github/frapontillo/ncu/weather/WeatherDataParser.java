package com.github.frapontillo.ncu.weather;

import android.text.format.Time;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataParser {

    private static final String LOG_TAG = WeatherDataParser.class.getSimpleName();

    private static final String OWM_LIST = "list";
    private static final String OWM_WEATHER = "weather";
    private static final String OWM_WEATHER_ID = "id";
    private static final String OWM_TEMPERATURE = "temp";
    private static final String OWM_MAX = "max";
    private static final String OWM_MIN = "min";
    private static final String OWM_DESCRIPTION = "main";
    private static final String OWM_PRESSURE = "pressure";
    private static final String OWM_HUMIDITY = "humidity";
    private static final String OWM_WINDSPEED = "speed";
    private static final String OWM_WIND_DIRECTION = "deg";

    private static final String OWM_CITY = "city";
    private static final String OWM_CITY_NAME = "name";
    private static final String OWM_COORD = "coord";
    private static final String OWM_LATITUDE = "lat";
    private static final String OWM_LONGITUDE = "lon";

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p/>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    @SuppressWarnings("deprecation")
    public static WeatherData getWeatherDataFromJson(String forecastJsonStr, int numDays, String zipCode)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.


        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONObject city = forecastJson.getJSONObject(OWM_CITY);
        String cityName = city.getString(OWM_CITY_NAME);
        JSONObject coordinates = city.getJSONObject(OWM_COORD);
        long latitude = coordinates.getLong(OWM_LATITUDE);
        long longitude = coordinates.getLong(OWM_LONGITUDE);

        WeatherLocation weatherLocation = WeatherLocation.create(zipCode, cityName, latitude, longitude);

        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.

        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        // now we work exclusively in UTC
        dayTime = new Time();

        List<WeatherDay> weatherDays = new ArrayList<>(numDays);
        for (int i = 0; i < weatherArray.length(); i++) {
            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            long dateTime = dayTime.setJulianDay(julianStartDay + i);
            Date date = new Date(dateTime);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            String description = weatherObject.getString(OWM_DESCRIPTION);
            int weatherId = weatherObject.getInt(OWM_WEATHER_ID);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = Math.round(temperatureObject.getDouble(OWM_MAX));
            double low = Math.round(temperatureObject.getDouble(OWM_MIN));

            double pressure = dayForecast.getDouble(OWM_PRESSURE);
            int humidity = dayForecast.getInt(OWM_HUMIDITY);
            double windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
            double windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);

            weatherDays.add(WeatherDay.create(date, weatherId, description, high, low, humidity, pressure, windDirection, windSpeed));
        }

        for (WeatherDay d : weatherDays) {
            Log.v(LOG_TAG, "Forecast entry: " + d);
        }
        return WeatherData.create(weatherLocation, weatherDays);

    }

}
