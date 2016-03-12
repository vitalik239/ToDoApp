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
import android.widget.EditText;
import android.widget.Toast;

import com.example.vitalik.myapplication.R;
import com.example.vitalik.todolist.database.DBContract;
import com.example.vitalik.todolist.database.DBEditor;
import com.example.vitalik.todolist.database.DBHelper;

public class ItemEditActivity extends AppCompatActivity {
    private EditText editTitle;
    private EditText editQuantity;
    private EditText editDescription;
    private String id;
    private DBEditor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editQuantity = (EditText) findViewById(R.id.editQuantity);
        editDescription = (EditText) findViewById(R.id.editDescription);

        id = getIntent().getStringExtra("id");
        showById(id);

        final Intent answerIntent = new Intent();
        setResult(RESULT_OK, answerIntent);

        final FloatingActionButton editFab = (FloatingActionButton) findViewById(R.id.fab);
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTitle.getText().length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Enter title!", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (editQuantity.getText().length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Enter quantity!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    editor = new DBEditor();
                    editor.delFromDatabase(MainActivity.mSqLiteDatabase,
                            Integer.parseInt(id));
                    String title = editTitle.getText().toString();
                    Integer quantity = Integer.parseInt(editQuantity.getText().toString());
                    String description = editDescription.getText().toString();

                    String newId = Long.toString(editor.addToDatabase(MainActivity.mSqLiteDatabase,
                            title, quantity, description));
                    answerIntent.putExtra("newId", newId);
                    Request.sendRequest(Long.parseLong(id), Long.parseLong(newId), title, quantity, description);
                    finish();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showById(String id) {
        String[] show = {
                BaseColumns._ID,
                DBContract.Columns.TITLE,
                DBContract.Columns.DESCRIPTION,
                DBContract.Columns.QUANTITY
        };

        Cursor c = null;
        try {
            c = MainActivity.mSqLiteDatabase.query(
                    DBContract.DATABASE_TABLE,
                    show,
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

        editTitle.setText(title.toCharArray(), 0, title.length());
        editQuantity.setText(quantity.toCharArray(), 0, quantity.length());
        editDescription.setText(description.toCharArray(), 0, description.length());
    }


}
