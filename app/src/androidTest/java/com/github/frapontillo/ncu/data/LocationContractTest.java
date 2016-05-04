package com.github.frapontillo.ncu.data;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import com.github.frapontillo.ncu.data.contract.LocationContract;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class LocationContractTest {
    private LocationContract locationContract;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        locationContract = new LocationContract();
    }

    @Test
    public void givenLocationId_whenBuildItemUri_thenReturnValidUri() {
        String id = "1337";

        Uri uri = locationContract.buildItemUri(1337);

        assertEquals(Uri.parse("content://com.github.frapontillo.ncu/location/" + id), uri);
    }

    @Test
    public void givenLocationSingleUri_whenGetItemId_thenReturnId() {
        Uri uri = Uri.parse("content://com.github.frapontillo.ncu/location/1337");

        long id = locationContract.getId(uri);

        assertEquals(1337, id);
    }

    @Test
    public void givenLocationMainUri_whenGetId_thenThrowException() {
        Uri uri = Uri.parse("content://com.github.frapontillo.ncu/location/");

        expectedException.expect(IndexOutOfBoundsException.class);
        locationContract.getId(uri);
    }
}
