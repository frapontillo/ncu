package com.github.frapontillo.ncu.data.contract;

import android.net.Uri;

public class Contract {
    public static final String CONTENT_AUTHORITY = "com.github.frapontillo.ncu";
    public static final Uri BASE_CONTENT_URI = new Uri.Builder().scheme("content").authority(CONTENT_AUTHORITY).build();
}
