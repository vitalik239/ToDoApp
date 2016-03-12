package com.example.vitalik.todolist.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DBShow {

    public Cursor searchByName(SQLiteDatabase db, String inputText) {
        Cursor mCursor = null;
        if ((inputText == null) || (inputText.length() == 0)) {
            mCursor = db.query(DBContract.DATABASE_TABLE, new String[]{DBContract.Columns._ID,
                            DBContract.Columns.TITLE, DBContract.Columns.QUANTITY, DBContract.Columns.DESCRIPTION},
                    null, null, null, null, null);
        } else {
            mCursor = db.query(true, DBContract.DATABASE_TABLE, new String[]{DBContract.Columns._ID,
                            DBContract.Columns.TITLE, DBContract.Columns.QUANTITY, DBContract.Columns.DESCRIPTION},
                    DBContract.Columns.TITLE + " like '%" + inputText + "%'", null,
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
}
