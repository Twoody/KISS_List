package com.example.tanner.taskapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Pop_tasks extends Activity{
    protected TaskerDBHelper db;
    MyAdapter adapt;
    List<Tasker> list;
    Button button;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        //Get `category` from `catBut` and homescreen a couple activities back;
        Intent parentIntent = getIntent();
        Bundle parentBD = parentIntent.getExtras();
        if (parentBD != null)
            category = (String) parentBD.get("category");
        else
            category = "";

        DisplayMetrics dimensions = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimensions);
        int width      = dimensions.widthPixels;
        int height     = dimensions.heightPixels;
        double width2  = width  * 0.8;
        double height2 = height * 0.4;
        width          = (int) width2;
        height         = (int) height2;
        getWindow().setLayout(width, height);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        db             = new TaskerDBHelper(this);
        list           = db.getAllTasks("tasksTable", category);
        adapt          = new MyAdapter(this, R.layout.list_inner_view, list);
        button         = findViewById(R.id.button_addTaskToDB);
        EditText input = (EditText) findViewById(R.id.editText_tasks);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskNow(v);
                finish();
            }
        });
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addTaskNow(v);
                    return true;
                }
                return false;
            }
        });
    }//end onCreate()

    public void addTaskNow(View v){
        Resources res = getResources();
        boolean debug = res.getBoolean(R.bool.debug);
        EditText t    = findViewById(R.id.editText_tasks);
        String s1     = category;
        String s2     = t.getText().toString();
        if (s1.equalsIgnoreCase("") && s2.equalsIgnoreCase("") ){
            Toast.makeText(this, "enter the task description first!!", Toast.LENGTH_LONG);
        }
        else {
            Tasker task = new Tasker(s1, s2, 0, 1);
            db.addTask(task);
            adapt.add(task);
            Log.d("task", "task added");
            if (debug == true){
                String msg = "\nDEBUG:\t"+ task + "\n";
                msg += "\n\tID:\t\t"       + task.getId();
                msg += "\n\tCATEGORY:\t"   + task.getCategory();
                msg += "\n\tCONTENT:\t"    + task.getContent();
                msg += "\n\tSTATUS:\t"     + task.getStatus();
                msg += "\n\tPLACE:\t"      + task.getPlace();
                Log.d("listener: POP_TASKS", msg);
            }
            t.setText("");
            adapt.add(task);
            adapt.notifyDataSetChanged();
        }
        finish();
    }//end addTaskNow()
}