package com.github.frapontillo.ncu.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WeatherDbHelper extends SQLiteOpenHelper {

    private static final char LINE_SEPARATOR = '\n';
    private static final String LOG_TAG = WeatherDbHelper.class.getSimpleName();
    private static final int DATABASE_INITIAL_NON_EXISTING_VERSION = 0;
    private static final int DATABASE_CURRENT_VERSION = 1;
    private static final String DATABASE_NAME = "Weather.db";

    private final Context context;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_CURRENT_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, DATABASE_INITIAL_NON_EXISTING_VERSION, DATABASE_CURRENT_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeFrom = Math.max(0, oldVersion) + 1;
        if (upgradeFrom > newVersion) {
            return;
        }
        for (int i = upgradeFrom; i <= newVersion; i++) {
            String versionedUpgradeScript = readVersionedUpgradeFromAssets(i);
            if (versionedUpgradeScript == null) {
                continue;
            }
            versionedUpgradeScript = versionedUpgradeScript.trim();
            try {
                for (String sql: getStatementsFrom(versionedUpgradeScript)) {
                    db.execSQL(sql);
                }
            } catch (SQLException e) {
                Log.w(LOG_TAG, String.format("Found an invalid database upgrade at iteration %d, ignoring.", i));
            }
        }
    }

    private String[] getStatementsFrom(String versionedUpgradeScript) {
        return versionedUpgradeScript.split(";");
    }

    private String readVersionedUpgradeFromAssets(int upgradeIteration) {
        InputStream inputStream;
        String script = null;
        try {
            inputStream = context.getAssets().open("sql/" + String.valueOf(upgradeIteration) + ".sql");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder scriptBuilder = new StringBuilder(inputStream.available());
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                scriptBuilder.append(line);
                scriptBuilder.append(LINE_SEPARATOR);
            }
            script = scriptBuilder.toString();
        } catch (IOException e) {
            Log.w(LOG_TAG, String.format("Could not read database upgrade at iteration %d, ignoring.", upgradeIteration));
        }
        return script;
    }
}
