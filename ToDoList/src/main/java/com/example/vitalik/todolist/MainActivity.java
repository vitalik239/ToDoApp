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
import android.widget.TextView;

import com.example.vitalik.myapplication.R;
import com.example.vitalik.todolist.database.DBContract;
import com.example.vitalik.todolist.database.DBEditor;
import com.example.vitalik.todolist.database.DBHelper;
import com.example.vitalik.todolist.database.DBShow;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static DBHelper mDBHelper;
    public static DBShow shower;
    public static DBEditor editor;
    public static SQLiteDatabase mSqLiteDatabase;
    public static SimpleCursorAdapter mAdapter;
    public static ListView lvData;
    private static final int returnCode = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DBHelper(this, DBContract.DATABASE_NAME, null, 1);
        mSqLiteDatabase = mDBHelper.getWritableDatabase();

        String[] from = new String[]{DBContract.Columns.TITLE};
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
                Intent intent = new Intent(MainActivity.this, ItemShowActivity.class);
                Log.w("MyLog", Long.toString(mAdapter.getItemId(position)));
                intent.putExtra("id", Long.toString(mAdapter.getItemId(position)));
                startActivityForResult(intent, returnCode);
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
                shower = new DBShow();
                return shower.searchByName(mSqLiteDatabase, constraint.toString());
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.fab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ItemAddActivity.class);
                startActivityForResult(intent, returnCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == returnCode) {
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
        menu.add(0, DBContract.DELETE_ID, 0, R.string.delete_record);
        menu.add(0, DBContract.EDIT_ID, 0, R.string.edit_record);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == DBContract.EDIT_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            Intent intent = new Intent(MainActivity.this, ItemEditActivity.class);
            intent.putExtra("id", "" + acmi.id);
            startActivityForResult(intent, 100);
            return true;
        } else if (item.getItemId() == DBContract.DELETE_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            editor = new DBEditor();
            editor.delFromDatabase(mSqLiteDatabase, acmi.id);
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
            shower = new DBShow();
            return shower.getAllData(mSqLiteDatabase);
        }
    }


    public void onDoneClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.lvText);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                DBContract.DATABASE_TABLE,
                DBContract.Columns.TITLE,
                task);


        SQLiteDatabase sqlDB = mDBHelper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateCursor();
    }

}
