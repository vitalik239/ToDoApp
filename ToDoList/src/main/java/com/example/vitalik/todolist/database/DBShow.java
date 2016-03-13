package com.example.vitalik.todolist.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.vitalik.todolist.Item;
import com.example.vitalik.todolist.MainActivity;

public class DBShow {

    public Cursor searchByName(SQLiteDatabase db, String name) {
        Cursor mCursor = null;
        if ((name == null) || (name.length() == 0)) {
            mCursor = db.query(DBContract.DATABASE_TABLE, new String[]{DBContract.Columns._ID,
                            DBContract.Columns.TITLE, DBContract.Columns.DESCRIPTION},
                    null, null, null, null, null);
        } else {
            mCursor = db.query(true, DBContract.DATABASE_TABLE, new String[]{DBContract.Columns._ID,
                            DBContract.Columns.TITLE, DBContract.Columns.DESCRIPTION},
                    DBContract.Columns.TITLE + " like '%" + name + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getAllData(SQLiteDatabase db) {
        return db.query(DBContract.DATABASE_TABLE, null, null, null, null, null, null);
    }

    public Item getItemByID(String id) {
        String[] dbColumnsToShow = {
                BaseColumns._ID,
                DBContract.Columns.TITLE,
                DBContract.Columns.DESCRIPTION,
                DBContract.Columns.LAT,
                DBContract.Columns.LNG
        };

        Cursor cursor = null;
        String title = "";
        String description = "";

        try {
            cursor = MainActivity.mSqLiteDatabase.query(
                    DBContract.DATABASE_TABLE,
                    dbColumnsToShow,
                    BaseColumns._ID + " = " + id,
                    null,
                    null,
                    null,
                    ""
            );
            cursor.moveToFirst();
            title = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Columns.TITLE)
            );
            description = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Columns.DESCRIPTION)
            );

            cursor.close();
        } catch (NullPointerException ex) {
            throw ex;
        }
        return new Item(title, description);
    }


}
