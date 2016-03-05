package com.example.vitalik.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {
    public static final String DATABASE_NAME = "mydatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "Catalog";
    public static final int DELETE_ID = 2;
    public static final int EDIT_ID = 1;
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

    public Cursor searchByName(SQLiteDatabase db, String inputText) {
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = db.query(DATABASE_TABLE, new String[] { BaseColumns._ID,
                             TITLE_COLUMN, QUANTITY_COLUMN, DESCRIPTION_COLUMN},
                    null, null, null, null, null);
        }
        else {
            mCursor = db.query(true, DATABASE_TABLE, new String[] { BaseColumns._ID,
                             TITLE_COLUMN, QUANTITY_COLUMN, DESCRIPTION_COLUMN},
                    TITLE_COLUMN + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public Cursor getAllData(SQLiteDatabase db) {
        return db.query(DATABASE_TABLE, null, null, null, null, null, null);
    }

    public void delFromDatabase(SQLiteDatabase db, long id) {
        db.delete(DATABASE_TABLE, BaseColumns._ID + " = " + id, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public long addToDatabase(SQLiteDatabase db, String title, Integer quantity, String description) {
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseHelper.TITLE_COLUMN, title);
        newValues.put(DatabaseHelper.QUANTITY_COLUMN, quantity);
        newValues.put(DatabaseHelper.DESCRIPTION_COLUMN, description);

        Log.w("MyLog", "added " + title);
        return db.insert(DATABASE_TABLE, null, newValues);
    }
}
