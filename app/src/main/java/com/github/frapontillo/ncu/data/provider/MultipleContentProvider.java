package com.github.frapontillo.ncu.data.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MultipleContentProvider extends ContentProvider {

    private final UriMatcher uriMatcher;
    private final Map<Integer, PartialProvider> partialProviderMap;
    private ContentResolver contentResolver;
    private int code;

    public MultipleContentProvider() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        code = -1;
        partialProviderMap = new ConcurrentHashMap<>();
    }

    public synchronized MultipleContentProvider addPartialContentProvider(String uriAuthority,
                                                                          String uriPath,
                                                                          PartialProvider partialContentProvider) {
        code += 1;
        uriMatcher.addURI(uriAuthority, uriPath, code);
        partialProviderMap.put(code, partialContentProvider);
        return this;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onCreate() {
        contentResolver = getContext().getContentResolver();
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return getUriContentProvider(uri).getType();
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return getUriContentProvider(uri).query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return insert(uri, values, true);
    }

    private Uri insert(Uri uri, ContentValues values, boolean notify) {
        Uri result = getUriContentProvider(uri).insert(uri, values);
        if (notify && result != null) {
            contentResolver.notifyChange(uri, null);
        }
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int result = getUriContentProvider(uri).delete(uri, selection, selectionArgs);
        if (result > 0) {
            contentResolver.notifyChange(uri, null);
        }
        return result;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int result = getUriContentProvider(uri).update(uri, values, selection, selectionArgs);
        if (result > 0) {
            contentResolver.notifyChange(uri, null);
        }
        return result;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = getUriContentProvider(uri).getHelper().getWritableDatabase();
        int insertCount = 0;
        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                Uri insertedUri = insert(uri, value, false);
                if (insertedUri != null) {
                    insertCount += 1;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        if (insertCount > 0) {
            contentResolver.notifyChange(uri, null);
        }
        return insertCount;
    }

    private PartialProvider getUriContentProvider(Uri uri) {
        int code = uriMatcher.match(uri);
        return partialProviderMap.get(code);
    }

}
