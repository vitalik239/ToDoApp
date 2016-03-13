package com.example.vitalik.todolist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vitalik.todolist.R;
import com.example.vitalik.todolist.database.DBEditor;

public class ItemAddActivity extends AppCompatActivity {
    private DBEditor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText editTitle = (EditText) findViewById(R.id.editTextTitle);
        final EditText editDescription = (EditText) findViewById(R.id.editTextDescription);

        setResult(RESULT_OK);

        FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.fab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputParser parser = new InputParser();
                String title = editTitle.getText().toString();
                if (parser.notEmpty(title)) {
                    String description = editDescription.getText().toString();
                    Item item = new Item(title, description);

                    editor = new DBEditor();

                    long id = editor.addToDatabase(MainActivity.mSqLiteDatabase, item);
                    BackEndSender.sendRequest(id, item);
                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Enter title!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
