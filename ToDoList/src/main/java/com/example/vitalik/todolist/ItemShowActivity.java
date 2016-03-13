package com.example.vitalik.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vitalik.myapplication.R;
import com.example.vitalik.todolist.database.DBShow;

public class ItemShowActivity extends AppCompatActivity {
    private TextView Title;
    private TextView Description;
    private String id;
    private static final int returnCode = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id = getIntent().getStringExtra("id");

        Title = (TextView) findViewById(R.id.Title);
        Description = (TextView) findViewById(R.id.Description);

        ShowItemById(id);

        FloatingActionButton editItemFab = (FloatingActionButton) findViewById(R.id.fab);
        editItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemShowActivity.this, ItemEditActivity.class);
                intent.putExtra("id", id);
                startActivityForResult(intent, returnCode);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == RESULT_OK) && (requestCode == returnCode)) {
            ShowItemById(id);
        }
    }

    public void ShowItemById(String id) {
        setResult(RESULT_OK);
        DBShow shower = new DBShow();
        Item item = new Item();
        try {
            item = shower.getItemByID(id);
        } catch (NullPointerException ex) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Nothing found with such ID", Toast.LENGTH_SHORT);
            toast.show();
        }
        Title.setText(item.getTitle());
        Description.setText(item.getDescription());
    }
}
