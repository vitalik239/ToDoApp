package com.example.vitalik.todolist.database;

import android.provider.BaseColumns;

public class DBContract {
    public static final String DATABASE_NAME = "tasklist.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "Tasks";
    public static final int DELETE_ID = 2;
    public static final int EDIT_ID = 1;

    public class Columns {
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String QUANTITY = "quantity";
        public static final String _ID = BaseColumns._ID;
    }

}
