package com.github.frapontillo.ncu.data;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import com.github.frapontillo.ncu.data.contract.WeatherContract;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class WeatherContractTest {
    private static final String NULL_LOCATION = null;
    private static final Date NULL_DATE = null;
    private static final long VALID_UNIX_DATE_LONG = 1461542400000L;
    private static final String VALID_UNIX_DATE_STRING = String.valueOf(VALID_UNIX_DATE_LONG);
    private WeatherContract weatherContract;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        weatherContract = new WeatherContract();
    }

    @Test
    public void givenWeatherId_whenBuildItemUri_thenReturnValidUri() {
        String id = "1337";

        Uri uri = weatherContract.buildItemUri(1337);

        assertEquals(Uri.parse("content://com.github.frapontillo.ncu/weather/" + id), uri);
    }

    @Test
    public void givenValidLocation_whenBuildWeatherWithLocationUri_thenReturnEncodedUri() {
        String location = "London, UK";

        Uri uri = weatherContract.buildWeatherWithLocationUri(location);

        assertEquals(Uri.parse("content://com.github.frapontillo.ncu/weather/London%2C%20UK"), uri);
    }

    @Test
    public void givenNotValidLocation_whenBuildWeatherWithLocationUri_thenThrowException() {
        expectedException.expect(IllegalArgumentException.class);
        weatherContract.buildWeatherWithLocationUri(NULL_LOCATION);
    }

    @Test
    public void givenValidLocationAndDate_whenBuildWeatherWithLocationAndDateUri_thenReturnValidUri() {
        String location = "London, UK";
        Date date = givenValidDate();

        Uri uri = weatherContract.buildWeatherWithLocationAndDateUri(location, date);

        assertEquals(Uri.parse("content://com.github.frapontillo.ncu/weather/London%2C%20UK/" + VALID_UNIX_DATE_STRING), uri);
    }

    @Test
    public void givenValidLocationAndNotValidDate_whenBuildWeatherWithLocationAndDateUri_thenThrowException() {
        String location = "London, UK";

        expectedException.expect(IllegalArgumentException.class);
        weatherContract.buildWeatherWithLocationAndDateUri(location, NULL_DATE);
    }

    @Test
    public void givenNotValidLocationAndValidDate_whenBuildWeatherWithLocationAndDateUri_thenThrowException() {
        Date date = givenValidDate();

        expectedException.expect(IllegalArgumentException.class);
        weatherContract.buildWeatherWithLocationAndDateUri(NULL_LOCATION, date);
    }

    @Test
    public void givenWeatherSingleUri_whenGetItemId_thenReturnId() {
        Uri uri = Uri.parse("content://com.github.frapontillo.ncu/weather/1337");

        long id = weatherContract.getId(uri);

        assertEquals(1337, id);
    }

    @Test
    public void givenWeatherMainUri_whenGetId_thenThrowException() {
        Uri uri = Uri.parse("content://com.github.frapontillo.ncu/weather/");

        expectedException.expect(IndexOutOfBoundsException.class);
        weatherContract.getId(uri);
    }

    @Test
    public void givenWeatherForLocationUri_whenGetLocation_thenReturnLocation() {
        Uri uri = Uri.parse("content://com.github.frapontillo.ncu/weather/London%2C%20UK");

        String location = weatherContract.getLocation(uri);

        assertEquals("London, UK", location);
    }

    @Test
    public void givenWeatherMainUri_whenGetLocation_thenThrowException() {
        Uri uri = Uri.parse("content://com.github.frapontillo.ncu/weather/");

        expectedException.expect(IndexOutOfBoundsException.class);
        weatherContract.getLocation(uri);
    }

    @Test
    public void givenWeatherForLocationAndDateUri_whenGetDate_thenReturnDate() {
        Uri uri = Uri.parse("content://com.github.frapontillo.ncu/weather/London%2C%20UK/" + VALID_UNIX_DATE_STRING);

        long date = weatherContract.getDate(uri);

        assertEquals(VALID_UNIX_DATE_LONG, date);
    }

    @Test
    public void givenWeatherForLocationUri_whenGetDate_thenThrowException() {
        Uri uri = Uri.parse("content://com.github.frapontillo.ncu/weather/London%2C%20UK");

        expectedException.expect(IndexOutOfBoundsException.class);
        weatherContract.getDate(uri);
    }

    @Test
    public void givenWeatherMainUri_whenGetDate_thenThrowException() {
        Uri uri = Uri.parse("content://com.github.frapontillo.ncu/weather/");

        expectedException.expect(IndexOutOfBoundsException.class);
        weatherContract.getDate(uri);
    }

    private Date givenValidDate() {
        Calendar cal = new GregorianCalendar(2016, GregorianCalendar.APRIL, 25);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        return cal.getTime();
    }
}
