package com.example.vitalik.todolist.database;

import android.provider.BaseColumns;

public class DBContract {
    public static final String DATABASE_NAME = "database.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "Tasks";
    public class Columns {
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String _ID = BaseColumns._ID;
        public static final String LAT = "latitude";
        public static final String LNG = "longitude";
    }
}
