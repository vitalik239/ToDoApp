package com.example.vitalik.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static DatabaseHelper mDatabaseHelper;
    public static SQLiteDatabase mSqLiteDatabase;
    public static SimpleCursorAdapter mAdapter;
    public static ListView lvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new DatabaseHelper(this, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        String[] from = new String[]{DatabaseHelper.TITLE_COLUMN};
        int[] to = new int[]{R.id.lvText};

        mAdapter = new SimpleCursorAdapter(this, R.layout.item, null, from, to, 0);
        lvData = (ListView) findViewById(R.id.listView);
        lvData.setAdapter(mAdapter);
        registerForContextMenu(lvData);
        getSupportLoaderManager().initLoader(0, null, this);
        lvData.setClickable(true);
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, ItemShow.class);
                Log.w("MyLog", "" + mAdapter.getItemId(position));
                intent.putExtra("id", "" + mAdapter.getItemId(position));
                startActivityForResult(intent, 100);
            }
        });


        EditText myFilter = (EditText) findViewById(R.id.editText);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }
        });

        mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return mDatabaseHelper.searchByName(mSqLiteDatabase, constraint.toString());
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ItemAdd.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            Log.w("MyLog", "after activity");
            updateCursor();
        }
    }

    public void updateCursor() {
        getSupportLoaderManager().getLoader(0).forceLoad();
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DatabaseHelper.DELETE_ID, 0, R.string.delete_record);
        menu.add(0, DatabaseHelper.EDIT_ID, 0, R.string.edit_record);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == DatabaseHelper.EDIT_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            Intent intent = new Intent(MainActivity.this, ItemEdit.class);
            intent.putExtra("id", "" + acmi.id);
            startActivityForResult(intent, 100);
            return true;
        } else if (item.getItemId() == DatabaseHelper.DELETE_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            mDatabaseHelper.delFromDatabase(mSqLiteDatabase, acmi.id);
            updateCursor();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    protected void onDestroy() {
        super.onDestroy();
        mSqLiteDatabase.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader((Context)this, mSqLiteDatabase);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            updateCursor();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class MyCursorLoader extends CursorLoader {
        SQLiteDatabase db;
        public MyCursorLoader(Context context, SQLiteDatabase db) {
            super(context);
            this.db = db;
        }
        @Override
        public Cursor loadInBackground() {
            return mDatabaseHelper.getAllData(mSqLiteDatabase);
        }
    }
}
