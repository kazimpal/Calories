package com.example.kazim.calories;

/**
 * Created by Kazim on 09/10/2015.
 */

import android.provider.BaseColumns;

public final class FoodContract {
    public FoodContract() {}

    public static abstract class FoodEntry implements BaseColumns {
        public static final String TABLE_NAME = "food";
        public static final String COLUMN_NAME_NULLABLE = "nulls";
        public static final String COLUMN_FOOD_NAME = "food_name";
        public static final String COLUMN_FOOD_CALORIES = "food_calories";
    }
}