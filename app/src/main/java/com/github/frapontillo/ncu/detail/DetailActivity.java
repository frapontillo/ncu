package com.github.frapontillo.ncu.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.frapontillo.ncu.R;

public class DetailActivity extends AppCompatActivity {
    public final static String EXTRA_WEATHER_DATA = "EXTRA_WEATHER_DATA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }
}
