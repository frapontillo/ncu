package com.github.frapontillo.ncu.data.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public abstract class PartialProvider<T extends SQLiteOpenHelper> {
    private final T helper;

    public PartialProvider(T helper) {
        this.helper = helper;
    }

    public T getHelper() {
        return helper;
    }

    abstract String getType();

    abstract Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder);

    abstract Uri insert(Uri uri, ContentValues values);

    abstract int delete(Uri uri, String selection, String[] selectionArgs);

    abstract int update(Uri uri, ContentValues values, String selection, String[] selectionArgs);
}
