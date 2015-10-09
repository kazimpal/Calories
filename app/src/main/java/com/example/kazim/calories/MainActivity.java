package com.example.kazim.calories;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FilterQueryProvider;
import android.widget.NumberPicker;
import android.widget.AutoCompleteTextView;
import android.content.ContentValues;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    NumberPicker calPicker;
    AutoCompleteTextView actv;
    FoodDbHelper mDbHelper;

    final static int[] toView = new int[] { android.R.id.text1 };
    final static String[] fromCol = new String[] { FoodContract.FoodEntry.COLUMN_FOOD_NAME };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new FoodDbHelper(getApplicationContext());
        writeFoodEntry("Tuna", 200);
        writeFoodEntry("Protein Shake", 300);
        writeFoodEntry("Protein Bar", 500);
        writeFoodEntry("Apple", 100);
        writeFoodEntry("Chicken Breast", 250);

        calPicker = (NumberPicker) findViewById(R.id.caloriePicker);
        String[] calValues = new String[21];
        for(int i = 0; i < calValues.length; i++)
            calValues[i] = Integer.toString(i*50);
        calPicker.setMaxValue(calValues.length - 1);
        calPicker.setMinValue(0);
        calPicker.setDisplayedValues(calValues);

        actv = (AutoCompleteTextView) findViewById(R.id.foodTextView);
        SimpleCursorAdapter foodNameAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_dropdown_item_1line, null, fromCol, toView, 0);
        actv.setAdapter(foodNameAdapter);
        actv.setThreshold(1);

        foodNameAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public String convertToString(Cursor cursor) {
                final int columnIndex = cursor.getColumnIndexOrThrow(FoodContract.FoodEntry.COLUMN_FOOD_NAME);
                final String str = cursor.getString(columnIndex);
                return str;
            }
        });

        foodNameAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                Cursor cursor = null;
                try {
                    cursor = getMatchingFoods((constraint != null ? constraint.toString() : null));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return cursor;
            }
        });


    }

    private void writeFoodEntry(String name, Integer cals) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FoodContract.FoodEntry.COLUMN_FOOD_NAME, name);
        values.put(FoodContract.FoodEntry.COLUMN_FOOD_CALORIES, cals);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(FoodContract.FoodEntry.TABLE_NAME, FoodContract.FoodEntry.COLUMN_NAME_NULLABLE, values);
    }

    private Cursor getMatchingFoods(String constraint) throws SQLException {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String queryString = "SELECT " +
                FoodContract.FoodEntry._ID + ", " +
                FoodContract.FoodEntry.COLUMN_FOOD_NAME + ", " +
                FoodContract.FoodEntry.COLUMN_FOOD_CALORIES + " FROM " +
                FoodContract.FoodEntry.TABLE_NAME;

        if (constraint != null) {
            constraint = constraint.trim() + "%";
            queryString += " WHERE "+ FoodContract.FoodEntry.COLUMN_FOOD_NAME +" LIKE ?";
        }
        String params[] = { constraint };

        if (constraint == null) {
            params = null;
        }
        Cursor cursor = db.rawQuery(queryString, params);
        if (cursor != null) {
            this.startManagingCursor(cursor);
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }
}
