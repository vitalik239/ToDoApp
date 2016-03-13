package com.example.vitalik.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vitalik.myapplication.R;
import com.example.vitalik.todolist.database.DBEditor;
import com.example.vitalik.todolist.database.DBShow;

public class ItemEditActivity extends AppCompatActivity {
    private EditText editTitle;
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
        editDescription = (EditText) findViewById(R.id.editDescription);

        id = getIntent().getStringExtra("id");
        showEditedItem(id);

        final Intent answerIntent = new Intent();
        setResult(RESULT_OK, answerIntent);

        final FloatingActionButton editItemFab = (FloatingActionButton) findViewById(R.id.fab);
        editItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString();
                InputParser parser = new InputParser();
                if (parser.notEmpty(title)) {
                    editor = new DBEditor();

                    String description = editDescription.getText().toString();
                    Item item = new Item(title, description);
                    editor.updateItemInDatabase(MainActivity.mSqLiteDatabase, Integer.parseInt(id), item);

                    BackEndSender.sendRequest(Long.parseLong(id), item);
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

    public void showEditedItem(String id) {
        DBShow shower = new DBShow();
        Item item = new Item();
        try {
            item = shower.getItemByID(id);
        } catch (NullPointerException ex) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Nothing found with such ID", Toast.LENGTH_SHORT);
            toast.show();
        }
        editTitle.setText(item.getTitle());
        editDescription.setText(item.getDescription());
    }
}
