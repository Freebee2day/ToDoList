package com.example.phoebeapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT="item_text";
    public static final String KEY_ITEM_POSITION="item_position";
    public static final int EDIT_TEXT_CODE=20;
    List<String> items;

    Button btnadd;
    EditText edtxtTask;
    RecyclerView taskData;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnadd =findViewById(R.id.btnadd);
        edtxtTask=findViewById(R.id.edtxtTask);
        taskData=findViewById(R.id.taskData);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Task Removed!",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener=new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position"+position);
                Intent i= new Intent(MainActivity.this,EditActivity.class);
                i.putExtra(KEY_ITEM_TEXT,items.get(position));
                i.putExtra(KEY_ITEM_POSITION,position);
                startActivityForResult(i,EDIT_TEXT_CODE);
            }
        };

        itemsAdapter =new ItemsAdapter(items,onLongClickListener,onClickListener);
        taskData.setAdapter(itemsAdapter);
        taskData.setLayoutManager(new LinearLayoutManager(this));

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem=edtxtTask.getText().toString();
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size()-1);
                edtxtTask.setText("");
                Toast.makeText(getApplicationContext(),"New Task Added!",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode==EDIT_TEXT_CODE){
            String itemText=data.getStringExtra(KEY_ITEM_TEXT);
            int position= data.getExtras().getInt(KEY_ITEM_POSITION);

            items.set(position,itemText);
            itemsAdapter.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(),"Item updated successfully!",Toast.LENGTH_SHORT).show();

        }else{
            Log.w("MainActivity","Unknown call to onActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    private void loadItems() {
        try {
            items = new ArrayList<>(org.apache.commons.io.FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
            //items = new ArrayList<String>(FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
            //items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));

        }catch(IOException e){
            Log.e("MainActivity","Error reading items",e);
            items =new ArrayList<>();
        }
    }

    private void saveItems(){
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(),items);
            //FileUtils.writeLines(getDataFile(),items);
        }catch(IOException e){
            Log.e("MainActivity","Error writing items",e);
        }
    }
}