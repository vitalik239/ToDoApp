package com.example.vitalik.todolist.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBEditor {

    public void delFromDatabase(SQLiteDatabase db, long id) {
        db.delete(DBContract.DATABASE_TABLE, DBContract.Columns._ID + " = " + id, null);
    }

    public long addToDatabase(SQLiteDatabase db, String title, Integer quantity, String description) {
        ContentValues newValues = new ContentValues();
        newValues.put(DBContract.Columns.TITLE, title);
        newValues.put(DBContract.Columns.QUANTITY, quantity);
        newValues.put(DBContract.Columns.DESCRIPTION, description);

        Log.w("MyLog", "added " + title);
        return db.insert(DBContract.DATABASE_TABLE, null, newValues);
    }

}



