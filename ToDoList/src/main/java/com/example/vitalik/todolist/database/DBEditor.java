package com.example.vitalik.todolist.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vitalik.todolist.Item;

public class DBEditor {

    public void delFromDatabase(SQLiteDatabase db, long id) {
        db.delete(DBContract.DATABASE_TABLE, DBContract.Columns._ID + " = " + id, null);
    }

    public long addToDatabase(SQLiteDatabase db, Item item) {
        ContentValues newValues = new ContentValues();
        newValues.put(DBContract.Columns.TITLE, item.getTitle());
        newValues.put(DBContract.Columns.DESCRIPTION, item.getDescription());

        Log.w("MyLog", "added " + item.getTitle());
        return db.insert(DBContract.DATABASE_TABLE, null, newValues);
    }

    public void updateItemInDatabase(SQLiteDatabase db, long id, Item item) {
        ContentValues newValues = new ContentValues();
        newValues.put(DBContract.Columns.TITLE, item.getTitle());
        newValues.put(DBContract.Columns.DESCRIPTION, item.getDescription());

        db.update(DBContract.DATABASE_TABLE, newValues, DBContract.Columns._ID + " = " + id, null);
    }

}



