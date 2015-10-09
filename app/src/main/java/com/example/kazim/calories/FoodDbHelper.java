package com.example.kazim.calories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Kazim on 09/10/2015.
 */

public class FoodDbHelper extends SQLiteOpenHelper {

    SQLiteDatabase mDb;

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + FoodContract.FoodEntry.TABLE_NAME + " ( " + FoodContract.FoodEntry._ID + " INTEGER PRIMARY KEY," +
            FoodContract.FoodEntry.COLUMN_FOOD_NAME + " TEXT, " +
            FoodContract.FoodEntry.COLUMN_FOOD_CALORIES + " INTEGER " +
            " )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FoodContract.FoodEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Foods.db";

    public FoodDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
