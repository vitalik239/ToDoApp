package com.example.vitalik.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "Catalog";

    public static final String TITLE_COLUMN = "title";
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String QUANTITY_COLUMN = "quantity";

    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + TITLE_COLUMN
            + " text not null, " + DESCRIPTION_COLUMN + " integer, "
            + QUANTITY_COLUMN + " text);";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public void addToDatabase(SQLiteDatabase db, String title, Integer quantity, String description) {
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseHelper.TITLE_COLUMN, title);
        newValues.put(DatabaseHelper.QUANTITY_COLUMN, quantity);
        newValues.put(DatabaseHelper.DESCRIPTION_COLUMN, description);
        db.insert(DATABASE_TABLE, null, newValues);
    }
}
