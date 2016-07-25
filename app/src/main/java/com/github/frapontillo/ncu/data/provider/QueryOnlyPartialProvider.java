package com.github.frapontillo.ncu.data.provider;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.github.frapontillo.ncu.data.UnsupportedUriOperationException;

public abstract class QueryOnlyPartialProvider<T extends SQLiteOpenHelper> extends PartialProvider<T> {

    public QueryOnlyPartialProvider(T helper) {
        super(helper);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedUriOperationException("insert", uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedUriOperationException("delete", uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedUriOperationException("update", uri);
    }

}
