package com.example.vitalik.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ItemAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText editTitle = (EditText) findViewById(R.id.editTextTitle);
        final EditText editQuantity = (EditText) findViewById(R.id.editTextQuantity);
        final EditText editDescription = (EditText) findViewById(R.id.editTextDescription);

        setResult(RESULT_OK);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
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
                    String title = editTitle.getText().toString();
                    Integer quantity = Integer.parseInt(editQuantity.getText().toString());
                    String description = editDescription.getText().toString();

                    long id = MainActivity.mDatabaseHelper.addToDatabase(MainActivity.mSqLiteDatabase,
                            title, quantity, description);
                    Request.sendRequest(-1, id, title, quantity, description);
                    finish();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
