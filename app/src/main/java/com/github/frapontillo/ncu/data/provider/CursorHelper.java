package com.github.frapontillo.ncu.data.provider;

import android.database.Cursor;

import java.util.Date;

public class CursorHelper {

    public static int getInt(Cursor cursor, String columnName) {
        int columnIndex = getColumnIndex(cursor, columnName);
        return cursor.getInt(columnIndex);
    }

    public static double getDouble(Cursor cursor, String columnName) {
        int columnIndex = getColumnIndex(cursor, columnName);
        return cursor.getDouble(columnIndex);
    }

    public static Date getDate(Cursor cursor, String columnName) {
        int columnIndex = getColumnIndex(cursor, columnName);
        long time = cursor.getLong(columnIndex);
        return new Date(time);
    }

    public static String getString(Cursor cursor, String columnName) {
        int columnIndex = getColumnIndex(cursor, columnName);
        return cursor.getString(columnIndex);
    }

    private static int getColumnIndex(Cursor cursor, String columnName) {
        return cursor.getColumnIndex(columnName);
    }

}
