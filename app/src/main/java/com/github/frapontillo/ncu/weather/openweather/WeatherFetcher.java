package com.github.frapontillo.ncu.weather.openweather;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.github.frapontillo.ncu.BuildConfig;
import com.github.frapontillo.ncu.data.contract.LocationContract;
import com.github.frapontillo.ncu.data.contract.WeatherContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;

@Deprecated
public class WeatherFetcher {
    private final String LOG_TAG = WeatherFetcher.class.getSimpleName();
    private final ContentResolver contentResolver;
    private final LocationContract locationContract;
    private final WeatherContract weatherContract;

    public WeatherFetcher(Context context) {
        contentResolver = context.getContentResolver();
        locationContract = new LocationContract();
        weatherContract = new WeatherContract();
    }

    public WeatherData getWeatherInfo(String zipCode) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            Uri uri = Uri.parse("http://api.openweathermap.org/data/2.5/forecast/daily").buildUpon()
                    .appendQueryParameter("q", zipCode)
                    .appendQueryParameter("mode", "json")
                    .appendQueryParameter("units", "metric")
                    .appendQueryParameter("cnt", "7")
                    .appendQueryParameter("appid", BuildConfig.OPEN_WEATHER_MAP_API_KEY).build();
            URL url = new URL(uri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Add line for debugging purposes
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            WeatherData data = WeatherDataParser.getWeatherDataFromJson(forecastJsonStr, 7, zipCode);
            return data;
        } catch (JSONException e) {
            return null;
        }
    }

}
