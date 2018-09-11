package com.beWoody.tanner.KISS_List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Pop_renameContent  extends Activity {
    protected TaskerDBHelper db;
    Button button;
    int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rename_task);

        //Get `taskId` from activity_manage_tasks back;
        Intent parentIntent = getIntent();
        Bundle parentBD = parentIntent.getExtras();
        if (parentBD != null)
            taskId = (int) parentBD.get("taskId");
        else
            taskId = 0;

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

        db              = new TaskerDBHelper(this);
        Tasker thisTask = db.getTask(taskId);
        button          = findViewById(R.id.button_updateTaskContent);
        EditText input  = (EditText) findViewById(R.id.editText_renameTasks);

        input.setText(thisTask.getContent());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renameTaskContentNow(v);
                finish();
            }
        });
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    renameTaskContentNow(v);
                    return true;
                }
                return false;
            }
        });
    }//end onCreate()

    public void renameTaskContentNow(View v){
        EditText t        = findViewById(R.id.editText_renameTasks);
        String newContent = t.getText().toString();
        if (newContent.equalsIgnoreCase("") )
            Toast.makeText(this, "enter the task description first!!", Toast.LENGTH_LONG);
        else{
            db.updateTaskContent(taskId, newContent);
            t.setText("");
        }
        finish();
    }//end addTaskNow()


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }//end onBackPressed()

    @Override
    public void onDestroy(){
        db.close();
        super.onDestroy();
    }

}