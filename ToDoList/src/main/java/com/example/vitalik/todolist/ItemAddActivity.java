package com.example.vitalik.todolist;

import com.example.vitalik.todolist.database.DBContract;
import com.example.vitalik.todolist.database.DBEditor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vitalik.myapplication.R;

public class ItemAddActivity extends AppCompatActivity {
    private DBEditor editor;

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

        FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.fab);
        addFab.setOnClickListener(new View.OnClickListener() {
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

                    editor = new DBEditor();

                    long id = editor.addToDatabase(MainActivity.mSqLiteDatabase,
                            title, quantity, description);
                    Request.sendRequest(-1, id, title, quantity, description);
                    finish();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
