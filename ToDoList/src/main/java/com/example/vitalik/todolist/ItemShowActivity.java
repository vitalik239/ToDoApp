package com.example.vitalik.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.vitalik.myapplication.R;
import com.example.vitalik.todolist.database.DBContract;

public class ItemShowActivity extends AppCompatActivity {
    private TextView Title;
    private TextView Quantity;
    private TextView Description;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id = getIntent().getStringExtra("id");

        Title = (TextView) findViewById(R.id.Title);
        Quantity = (TextView) findViewById(R.id.Quantity);
        Description = (TextView) findViewById(R.id.Description);

        ShowById(id);

        FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.fab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemShowActivity.this, ItemEditActivity.class);
                intent.putExtra("id", id);
                startActivityForResult(intent, 200);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200) {
            Log.w("MyLog", "fail in id");

            id = data.getStringExtra("newId");
            Log.w("MyLog", "id = " + id);
            ShowById(id);
        }
    }

    public void ShowById(String id) {
        String[] projection = {
                BaseColumns._ID,
                DBContract.Columns.TITLE,
                DBContract.Columns.DESCRIPTION,
                DBContract.Columns.QUANTITY
        };


        Cursor c = null;
        try {
            c = MainActivity.mSqLiteDatabase.query(
                    DBContract.DATABASE_TABLE,
                    projection,
                    BaseColumns._ID + " = " + id,
                    null,
                    null,
                    null,
                    ""
            );
        } catch (Exception ex) {
            Log.w("MyLog", ex.toString());
        }

        Log.w("MyLog", "cursor found");
        c.moveToFirst();
        String title = c.getString(
                c.getColumnIndexOrThrow(DBContract.Columns.TITLE)
        );
        String description = c.getString(
                c.getColumnIndexOrThrow(DBContract.Columns.DESCRIPTION)
        );
        String quantity = c.getString(
                c.getColumnIndexOrThrow(DBContract.Columns.QUANTITY)
        );

        setResult(RESULT_OK);

        Title.setText(title.toCharArray(), 0, title.length());
        Quantity.setText(quantity.toCharArray(), 0, quantity.length());
        Description.setText(description.toCharArray(), 0, description.length());
    }


}
