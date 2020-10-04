package com.example.phoebeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    EditText updateItem;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        updateItem=findViewById(R.id.updateItem);
        btnUpdate=findViewById(R.id.btnUpdate);

        getSupportActionBar().setTitle("Update The Task");

        updateItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();

                intent.putExtra(MainActivity.KEY_ITEM_TEXT,updateItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION,getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                setResult(RESULT_OK,intent);

                finish();

            }
        });

    }
}