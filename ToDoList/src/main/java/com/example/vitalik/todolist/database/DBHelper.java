package com.example.vitalik.todolist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DBContract.DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + DBContract.Columns.TITLE
            + " text not null, " + DBContract.Columns.DESCRIPTION + " text, "
            + DBContract.Columns.LAT + " double," + DBContract.Columns.LNG + " double);";

    DBHelper(Context context) {
        super(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.DATABASE_TABLE);
        onCreate(db);
    }
}
