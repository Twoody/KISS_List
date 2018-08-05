package com.example.tanner.taskapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Pop extends Activity{
    protected TaskerDBHelper db;
    CatAdapter adapt;
    List<Categories> list;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);

        DisplayMetrics dimensions = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimensions);
        int width      = dimensions.widthPixels;
        int height     = dimensions.heightPixels;
        double width2  = width  * 0.8;
        double height2 = height * 0.8;
        width          = (int) width2;
        height         = (int) height2;
        getWindow().setLayout(width, height);


        db     = new TaskerDBHelper(this);
        list   = db.getAllCategories();
        adapt  = new CatAdapter(this, R.layout.list_categories, list);
        button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskNow(v);
                openMainActivity();
            }
        });
    }//end onCreate()

    public void openMainActivity(){
        Intent intent = new Intent(this, activity_add_task.class);
        startActivity(intent);
    }//end openMainActivity()

    public void addTaskNow(View v){
        EditText t = findViewById(R.id.editText1);
        String s1  = t.getText().toString();
        String s2  = t.getText().toString();
        if (s1.equalsIgnoreCase("") && s2.equalsIgnoreCase("") ){
            Toast.makeText(this, "enter the task description first!!", Toast.LENGTH_LONG);
        }
        else {
            Categories cat = new Categories(s1, 1);
            db.addCat(cat);
            Log.d("cat", "adda added");
            t.setText("");
            adapt.add(cat);
            adapt.notifyDataSetChanged();
        }
    }//end addTaskNow()

}
